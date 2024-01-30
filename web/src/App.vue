<template>
  <NavBar />
  <router-view></router-view>
  <el-icon class="chatIcon" @click="openChatBox"><ChatDotRound /></el-icon>
  <el-dialog
    class="ChatBoxDialog"
    v-model="showChatBox"
    title="ChatBox"
    width="80%"
  >
    <ChatBox></ChatBox>
  </el-dialog>
</template>

<script>
import NavBar from "./components/NavBar.vue";
import ChatBox from "./components/ChatBox.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
import { ref, onMounted } from "vue";
// import { useStore } from "vuex";
// import $ from "jquery";

export default {
  components: {
    NavBar,
    ChatBox,
  },
  setup() {
    // const store = useStore();
    const showChatBox = ref(false);

    const openChatBox = () => {
      showChatBox.value = true;
    };
    onMounted(() => {});

    return {
      openChatBox,
      showChatBox,
    };
  },
};
</script>

<style>
body {
  font-family: Montserrat, sans-serif;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  background-image: url("./assets/images/main.png");
  background-size: cover;
  width: 100%;
  height: 100%;
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: fixed;
}
.chatIcon {
  position: fixed !important;
  bottom: 10px !important;
  right: 10px !important;
  color: green !important;
  font-size: 25px !important;
}

.chatIcon:hover {
  color: aliceblue !important;
}

.ChatBoxDialog {
  padding: 0 !important;
}

.el-dialog__body {
  padding: 0 !important;
}
</style>
