<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
      Draw
    </div>
    <div
      class="result-board-text"
      v-else-if="
        $store.state.pk.loser === 'A' &&
        $store.state.pk.a_id == $store.state.user.id // 由于两个id类型不一样，需要用 == 判断
      "
    >
      Lose
    </div>
    <div
      class="result-board-text"
      v-else-if="
        $store.state.pk.loser === 'B' &&
        $store.state.pk.b_id == $store.state.user.id
      "
    >
      Lose
    </div>
    <div class="result-board-text" v-else>Win</div>

    <div class="result-board-btn">
      <button @click="restart" type="button">再来!</button>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
export default {
  name: "ResultBoard",
  setup() {
    const store = useStore();
    const restart = () => {
      store.commit("updateStatus", "matching");
      store.commit("updateLoser", "none");
      store.commit("updateOpponent", {
        username: "我的对手",
        photo:
          "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      });
    };
    return {
      restart,
    };
  },
};
</script>

<style scoped>
div.result-board {
  height: 40vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto;
}
div.result-board-text {
  text-align: center;
  color: white;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}
div.result-board-btn {
  text-align: center;
  padding-top: 7vh;
}

.result-board-btn > button {
  font-size: 20px;
  border-radius: 5px;
  background-color: #ffc310;
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  user-select: none;
}
</style>
