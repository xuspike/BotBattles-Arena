<template>
  <button class="custom-btn btn-4" @click="click_ESC">
    <span>返回 Esc</span>
  </button>
  <div class="matchground">
    <div class="row">
      <div class="col-4">
        <div class="user-photo">
          <img :src="$store.state.user.photo" alt="" />
        </div>
        <div class="user-username">
          {{ $store.state.user.username }}
        </div>
      </div>

      <div class="col-4">
        <div class="user-select-bot" style="margin-top: 35%; margin-right: 2vw">
          <select
            v-model="select_bot"
            name="form-select"
            aria-label="Default select example"
          >
            <option value="-1" selected>亲自上阵</option>
            <option v-for="bot in bots" :key="bot.id" :value="bot.id">
              {{ bot.title }}
            </option>
          </select>
        </div>
      </div>

      <div class="col-4">
        <div class="user-photo">
          <img :src="$store.state.pk.opponent_photo" alt="" />
        </div>
        <div class="user-username">
          {{ $store.state.pk.opponent_username }}
        </div>
      </div>
    </div>
    <div class="col-12" style="text-align: center; padding-top: 15vh">
      <button type="button" class="custom-btn btn-6" @click="click_match_btn">
        <span>{{ match_btn_info }}</span>
      </button>
    </div>
  </div>
</template>

<script>
import $ from "jquery";
import { ref } from "vue";
import { useStore } from "vuex";
export default {
  name: "MatchGround",
  setup() {
    const store = useStore();
    let match_btn_info = ref("开始匹配");
    let bots = ref([]);
    let select_bot = ref("-1");

    const click_match_btn = () => {
      if (match_btn_info.value === "开始匹配") {
        match_btn_info.value = "取消";
        store.commit("updateLoser", "none");
        store.state.pk.socket.send(
          JSON.stringify({
            event: "start-matching",
            bot_id: select_bot.value,
            mode: store.state.pk.mode,
          })
        );
      } else {
        match_btn_info.value = "开始匹配";
        store.state.pk.socket.send(
          JSON.stringify({
            event: "stop-matching",
            mode: store.state.pk.mode,
          })
        );
      }
    };

    const refresh_bots = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp;
        },
      });
    };

    refresh_bots();

    const click_ESC = () => {
      store.commit("updateStatus", "game-selecting");
    };

    return {
      match_btn_info,
      click_match_btn,
      bots,
      select_bot,
      click_ESC,
    };
  },
};
</script>

<style scoped lang="scss">
div.matchground {
  width: 60vw;
  height: 70vh;
  margin: 40px auto;
  background-color: rgba(50, 50, 50, 0.5);
}

div.user-photo {
  text-align: center;
  padding-top: 10vh;
}
div.user-photo > img {
  border-radius: 50%;
  width: 20vh;
}
div.user-username {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: white;
  padding-top: 2vh;
}
:root {
  --background-gradient: linear-gradient(30deg, #f39c12 30%, #f1c40f);
  --gray: #34495e;
  --darkgray: #2c3e50;
}

select {
  /* Reset Select */
  appearance: none;
  outline: 0;
  border: 0;
  box-shadow: none;
  /* Personalize */
  flex: 1;
  padding: 0 1em;
  color: #fff;
  background-color: #2c3e50;
  background-image: none;
  cursor: pointer;
}
/* Remove IE arrow */
select::-ms-expand {
  display: none;
}
/* Custom Select wrapper */
.user-select-bot {
  position: relative;
  display: flex;
  width: 18vw;
  height: 3em;
  border-radius: 0.25em;
  overflow: hidden;
}
/* Arrow */
.user-select-bot::after {
  content: "\25BC";
  position: absolute;
  top: 0;
  right: 0;
  padding: 1em;
  background-color: #34495e;
  transition: 0.25s all ease;
  pointer-events: none;
}
/* Transition */
.user-select-bot:hover::after {
  color: #f39c12;
}

.custom-btn {
  margin-top: 10px;
  margin-left: 10px;
  width: 130px;
  height: 40px;
  color: #fff;
  border-radius: 5px;
  padding: 10px 25px;
  font-family: "Lato", sans-serif;
  font-weight: 500;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, 0.5),
    7px 7px 20px 0px rgba(0, 0, 0, 0.1), 4px 4px 5px 0px rgba(0, 0, 0, 0.1);
  outline: none;
}

.btn-4 {
  background-color: #4dccc6;
  background-image: linear-gradient(315deg, #4dccc6 0%, #96e4df 74%);
  line-height: 42px;
  padding: 0;
  border: none;
}
.btn-4:hover {
  background-color: #89d8d3;
  background-image: linear-gradient(315deg, #89d8d3 0%, #03c8a8 74%);
}
.btn-4 span {
  position: relative;
  display: block;
  width: 100%;
  height: 100%;
}
.btn-4:before,
.btn-4:after {
  position: absolute;
  content: "";
  right: 0;
  top: 0;
  box-shadow: 4px 4px 6px 0 rgba(255, 255, 255, 0.9),
    -4px -4px 6px 0 rgba(116, 125, 136, 0.2),
    inset -4px -4px 6px 0 rgba(255, 255, 255, 0.9),
    inset 4px 4px 6px 0 rgba(116, 125, 136, 0.3);
  transition: all 0.3s ease;
}
.btn-4:before {
  height: 0%;
  width: 0.1px;
}
.btn-4:after {
  width: 0%;
  height: 0.1px;
}
.btn-4:hover:before {
  height: 100%;
}
.btn-4:hover:after {
  width: 100%;
}
.btn-4 span:before,
.btn-4 span:after {
  position: absolute;
  content: "";
  left: 0;
  bottom: 0;
  box-shadow: 4px 4px 6px 0 rgba(255, 255, 255, 0.9),
    -4px -4px 6px 0 rgba(116, 125, 136, 0.2),
    inset -4px -4px 6px 0 rgba(255, 255, 255, 0.9),
    inset 4px 4px 6px 0 rgba(116, 125, 136, 0.3);
  transition: all 0.3s ease;
}
.btn-4 span:before {
  width: 0.1px;
  height: 0%;
}
.btn-4 span:after {
  width: 0%;
  height: 0.1px;
}
.btn-4 span:hover:before {
  height: 100%;
}
.btn-4 span:hover:after {
  width: 100%;
}

/* 6 */
.btn-6 {
  background: rgb(247, 150, 192);
  background: radial-gradient(
    circle,
    rgba(247, 150, 192, 1) 0%,
    rgba(118, 174, 241, 1) 100%
  );
  line-height: 42px;
  padding: 0;
  border: none;
}
.btn-6 span {
  position: relative;
  display: block;
  width: 100%;
  height: 100%;
}
.btn-6:before,
.btn-6:after {
  position: absolute;
  content: "";
  height: 0%;
  width: 1px;
  box-shadow: -1px -1px 20px 0px rgba(255, 255, 255, 1),
    -4px -4px 5px 0px rgba(255, 255, 255, 1),
    7px 7px 20px 0px rgba(0, 0, 0, 0.4), 4px 4px 5px 0px rgba(0, 0, 0, 0.3);
}
.btn-6:before {
  right: 0;
  top: 0;
  transition: all 500ms ease;
}
.btn-6:after {
  left: 0;
  bottom: 0;
  transition: all 500ms ease;
}
.btn-6:hover {
  background: transparent;
  color: #76aef1;
  box-shadow: none;
}
.btn-6:hover:before {
  transition: all 500ms ease;
  height: 100%;
}
.btn-6:hover:after {
  transition: all 500ms ease;
  height: 100%;
}
.btn-6 span:before,
.btn-6 span:after {
  position: absolute;
  content: "";
  box-shadow: -1px -1px 20px 0px rgba(255, 255, 255, 1),
    -4px -4px 5px 0px rgba(255, 255, 255, 1),
    7px 7px 20px 0px rgba(0, 0, 0, 0.4), 4px 4px 5px 0px rgba(0, 0, 0, 0.3);
}
.btn-6 span:before {
  left: 0;
  top: 0;
  width: 0%;
  height: 0.5px;
  transition: all 500ms ease;
}
.btn-6 span:after {
  right: 0;
  bottom: 0;
  width: 0%;
  height: 0.5px;
  transition: all 500ms ease;
}
.btn-6 span:hover:before {
  width: 100%;
}
.btn-6 span:hover:after {
  width: 100%;
}
</style>
