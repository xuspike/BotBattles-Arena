<template>
  <div
    class="toast show"
    role="alert"
    aria-live="assertive"
    aria-atomic="true"
    v-if="$store.state.pk.status === 'playing'"
    style="position: absolute; right: 0; width: 30vw"
  >
    <div v-if="$store.state.pk.mode === 'snake'" class="toast-header">
      <img
        style="width: 2vw; border-radius: 50%"
        src="@/assets/images/1_d7f3b93efd-kob.jpg"
        alt=""
      />
      <strong class="me-auto" style="margin-left: 5px">游戏开始</strong>
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="toast"
        aria-label="Close"
      ></button>
    </div>
    <div v-else-if="$store.state.pk.mode === 'gobang'" class="toast-header">
      <img
        style="width: 2vw; border-radius: 50%"
        src="@/assets/images/u=578129405,813708542&fm=253&fmt=auto&app=138&f=JPEG.webp"
        alt=""
      />
      <strong class="me-auto" style="margin-left: 5px">游戏开始</strong>
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="toast"
        aria-label="Close"
      ></button>
    </div>
    <div v-if="$store.state.pk.mode === 'snake'" class="toast-body">
      你出生在
      <span
        style="color: blue"
        v-if="parseInt($store.state.user.id) === $store.state.pk.a_id"
        >蓝方
      </span>
      <span
        style="color: red"
        v-else-if="parseInt($store.state.user.id) === $store.state.pk.b_id"
      >
        红方
      </span>
    </div>
    <div v-else-if="$store.state.pk.mode === 'gobang'" class="toast-body">
      你执手
      <span
        style="color: black"
        v-if="parseInt($store.state.user.id) === $store.state.pk.a_id"
        >黑棋
      </span>
      <span
        style="color: white"
        v-else-if="parseInt($store.state.user.id) === $store.state.pk.b_id"
      >
        白棋
      </span>
    </div>
  </div>
  <SelectGround v-if="$store.state.pk.status === 'game-selecting'" />
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
  <MatchGround v-if="$store.state.pk.status === 'matching'" />
  <ResultBoard v-if="$store.state.pk.loser !== 'none'" />
</template>

<script>
import $ from "jquery";
import PlayGround from "@/components/PlayGround.vue";
import MatchGround from "@/components/MatchGround.vue";
import ResultBoard from "@/components/ResultBoard.vue";
import SelectGround from "@/components/SelectGround.vue";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";

export default {
  components: {
    PlayGround,
    MatchGround,
    ResultBoard,
    SelectGround,
  },
  setup() {
    const store = useStore();
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
    let move_music = new Audio(require("@/assets/sound/蛇移动声音.wav"));
    let drop_music = new Audio(
      require("@/assets/sound/在棋盘上落子的声音.mp3")
    );
    let win_music = new Audio(require("@/assets/sound/游戏胜利音效.wav"));
    let lose_music = new Audio(require("@/assets/sound/游戏失败音效.wav"));

    store.commit("updateLoser", "none");
    store.commit("updateIsRecord", false);

    let socket = null;
    onMounted(() => {
      store.commit("updateOpponent", {
        username: "我的对手",
        photo:
          "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      });
      socket = new WebSocket(socketUrl);

      socket.onopen = () => {
        store.commit("updateSocket", socket);
      };

      const hide_toast = () => {
        $(".toast").fadeOut();
      };

      socket.onmessage = (msg) => {
        const data = JSON.parse(msg.data);
        if (data.event === "success-matching") {
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo,
          });
          // 秒换地图看不见对手
          setTimeout(() => {
            store.commit("updateStatus", "playing");
          }, 200);
          store.commit("updateGame", data.game);
        } else if (data.event === "move") {
          hide_toast();
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;
          snake0.set_direction(data.a_direction);
          snake1.set_direction(data.b_direction);
          if (move_music != null) {
            move_music.currentTime = 0;
            move_music.play();
          }
        } else if (data.event === "drop") {
          const game = store.state.pk.gameObject;
          const playerA = game.PlayerA;
          const playerB = game.PlayerB;
          if (data.player === "a") {
            playerA.push_chess(data.next_step);
            // 将上一步棋子状态改变(is_last = false)
            if (playerB.chesses.length > 0) {
              playerB.chesses[playerB.chesses.length - 1].is_last = false;
            }
          } else if (data.player === "b") {
            playerB.push_chess(data.next_step);
            if (playerA.chesses.length > 0) {
              playerA.chesses[playerA.chesses.length - 1].is_last = false;
            }
          }
          if (drop_music != null) {
            drop_music.currentTime = 0;
            drop_music.play();
          }
        } else if (data.event === "fall") {
          console.log(data);
          const game = store.state.pk.gameObject;
          const playerA = game.PlayerA;
          const playerB = game.PlayerB;
          if (data.player === "a") {
            playerA.fall_chess(data.next_step);
            // 将上一步棋子状态改变(is_last = false)
            if (playerB.chesses.length > 0) {
              playerB.chesses[playerB.chesses.length - 1].is_last = false;
            }
          } else if (data.player === "b") {
            playerB.fall_chess(data.next_step);
            if (playerA.chesses.length > 0) {
              playerA.chesses[playerA.chesses.length - 1].is_last = false;
            }
          }
        } else if (data.event === "snake_result") {
          const game = store.state.pk.gameObject;
          const [snake0, snake1] = game.snakes;

          if (data.loser === "all" || data.loser === "A") {
            snake0.status = "die";
          }
          if (data.loser === "all" || data.loser === "B") {
            snake1.status = "die";
          }
          if (data.loser === "A") {
            if (store.state.user.id == store.state.pk.a_id) lose_music.play();
            else win_music.play();
          } else if (data.loser === "B") {
            if (store.state.user.id == store.state.pk.a_id) win_music.play();
            else lose_music.play();
          }
          store.commit("updateLoser", data.loser);
        } else if (data.event === "gobang_result") {
          store.commit("updateLoser", data.loser);
          store.commit("updateWinnerDirection", data.winner_direction);
          if (data.loser === "A") {
            if (store.state.user.id == store.state.pk.a_id) lose_music.play();
            else win_music.play();
          } else if (data.loser === "B") {
            if (store.state.user.id == store.state.pk.a_id) win_music.play();
            else lose_music.play();
          }
        }
      };

      socket.onclose = () => {};
    });

    // 卸载时
    onUnmounted(() => {
      socket.close();
      store.commit("updateStatus", "game-selecting");
    });
  },
};
</script>

<style scoped>
div.user-color {
  text-align: center;
  color: white;
  font-size: 30px;
  font-weight: bold;
}
</style>
