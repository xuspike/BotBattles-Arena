<template>
  <div></div>
</template>

<script>
import router from "@/router/index";
import { useRoute } from "vue-router";
import { useStore } from "vuex";
import $ from "jquery";

export default {
  setup() {
    const myRoute = useRoute();
    const store = useStore();
    $.ajax({
      url: "http://127.0.0.1:3000/api/user/account/acwing/web/receive_code/",
      type: "get",
      data: {
        code: myRoute.query.code,
        state: myRoute.query.state,
      },
      success: (resp) => {
        // 成功到首页，否则到登录页面
        if (resp.result === "success") {
          localStorage.setItem("jwt_token", resp.jwt_token);
          store.commit("updateToken", resp.jwt_token);
          router.push({ name: "home" });
        } else {
          router.push({ name: "user_account_login" });
        }
      },
    });
  },
};
</script>

<style scoped></style>
