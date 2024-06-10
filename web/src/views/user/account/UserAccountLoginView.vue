<template>
  <div class="login-box">
    <h2>Login</h2>
    <form>
      <div class="user-box">
        <input type="text" name="" required="" v-model="username" />
        <label for="username">Username</label>
      </div>
      <div class="user-box">
        <input v-model="password" type="password" name="" required="" />
        <label for="password">Password</label>
      </div>

      <div style="text-align: right; user-select: none">
        <img
          width="25"
          src="https://wiki.connect.qq.com/wp-content/uploads/2013/10/03_qq_symbol-1-250x300.png"
          alt=""
          style="margin-right: 10px; cursor: pointer"
        />
        <img
          width="25"
          src="https://cdn.acwing.com/media/article/image/2022/09/06/1_32f001fd2d-acwing_logo.png"
          alt=""
          @click="acwing_login"
          style="cursor: pointer"
        />
      </div>
      <a href="#" @click="login">
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        Submit
      </a>
      <div class="error-message">{{ error_message }}</div>
    </form>
  </div>
</template>

<script>
import { useStore } from "vuex";
import { ref } from "vue";
import router from "../../../router/index";
import $ from "jquery";

export default {
  setup() {
    const store = useStore();
    let username = ref("");
    let password = ref("");
    let error_message = ref("");

    const login = () => {
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success() {
          store.dispatch("getinfo", {
            success() {
              router.push({ name: "home" });
            },
          });
        },
        error() {
          error_message.value = "用户名或密码错误！";
        },
      });
    };

    const acwing_login = () => {
      $.ajax({
        url: "https://xrookie.xyz/api/user/account/acwing/web/apply_code/",
        type: "get",
        success: (resp) => {
          if (resp.result === "success") {
            window.location.replace(resp.apply_code_url);
          }
        },
      });
    };

    return {
      username,
      password,
      error_message,
      login,
      acwing_login,
    };
  },
};
</script>

<style scoped>
button {
  width: 100%;
}

.login-box {
  position: absolute;
  top: 54%;
  left: 50%;
  width: 400px;
  padding: 40px;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.5);
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.6);
  border-radius: 10px;
}

.login-box h2 {
  margin: 0 0 30px;
  padding: 0;
  color: #fff;
  text-align: center;
}

.login-box .user-box {
  position: relative;
}

.login-box .user-box input {
  width: 100%;
  padding: 10px 0;
  font-size: 16px;
  color: #fff;
  margin-bottom: 30px;
  border: none;
  border-bottom: 1px solid #fff;
  outline: none;
  background: transparent;
}
.login-box .user-box label {
  position: absolute;
  top: 0;
  left: 0;
  padding: 10px 0;
  font-size: 16px;
  color: #fff;
  pointer-events: none;
  transition: 0.5s;
}

.login-box .user-box input:focus ~ label,
.login-box .user-box input:valid ~ label {
  top: -20px;
  left: 0;
  color: #03e9f4;
  font-size: 12px;
}

.login-box form a {
  position: relative;
  display: inline-block;
  padding: 10px 20px;
  color: #03e9f4;
  font-size: 16px;
  text-decoration: none;
  text-transform: uppercase;
  overflow: hidden;
  transition: 0.5s;
  margin-top: 30px;
  letter-spacing: 4px;
}

.login-box a:hover {
  background: #03e9f4;
  color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 5px #03e9f4, 0 0 25px #03e9f4, 0 0 50px #03e9f4,
    0 0 100px #03e9f4;
}

.login-box a span {
  position: absolute;
  display: block;
}

.login-box a span:nth-child(1) {
  top: 0;
  left: -100%;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, #03e9f4);
  animation: btn-anim1 1s linear infinite;
}

@keyframes btn-anim1 {
  0% {
    left: -100%;
  }
  50%,
  100% {
    left: 100%;
  }
}

.login-box a span:nth-child(2) {
  top: -100%;
  right: 0;
  width: 2px;
  height: 100%;
  background: linear-gradient(180deg, transparent, #03e9f4);
  animation: btn-anim2 1s linear infinite;
  animation-delay: 0.25s;
}

@keyframes btn-anim2 {
  0% {
    top: -100%;
  }
  50%,
  100% {
    top: 100%;
  }
}

.login-box a span:nth-child(3) {
  bottom: 0;
  right: -100%;
  width: 100%;
  height: 2px;
  background: linear-gradient(270deg, transparent, #03e9f4);
  animation: btn-anim3 1s linear infinite;
  animation-delay: 0.5s;
}

@keyframes btn-anim3 {
  0% {
    right: -100%;
  }
  50%,
  100% {
    right: 100%;
  }
}

.login-box a span:nth-child(4) {
  bottom: -100%;
  left: 0;
  width: 2px;
  height: 100%;
  background: linear-gradient(360deg, transparent, #03e9f4);
  animation: btn-anim4 1s linear infinite;
  animation-delay: 0.75s;
}

@keyframes btn-anim4 {
  0% {
    bottom: -100%;
  }
  50%,
  100% {
    bottom: 100%;
  }
}

div.error-message {
  position: relative;
  display: inline-block;
  padding: 10px 12px;
  color: #03e9f4;
  font-size: 13px;
  text-transform: uppercase;
  overflow: hidden;
  transition: 0.5s;
  margin-top: 30px;
  letter-spacing: 4px;
}
</style>
