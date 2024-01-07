<template>
  <div class="register-box">
    <h2>Register</h2>
    <form>
      <div class="user-box">
        <input type="text" name="" required="" v-model="username" />
        <label for="username">Username</label>
      </div>
      <div class="user-box">
        <input v-model="password" type="text" name="" required="" />
        <label for="password">Password</label>
      </div>

      <div class="user-box">
        <input v-model="confirmedPassword" type="text" name="" required="" />
        <label for="confirmedPassword">Confirm Password</label>
      </div>

      <a href="#" @click="register">
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
import { ref } from "vue";
import { useRouter } from "vue-router";
import $ from "jquery";

export default {
  setup() {
    const router = useRouter();
    let username = ref("");
    let password = ref("");
    let confirmedPassword = ref("");
    let error_message = ref("");

    const register = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/account/register/",
        type: "post",
        data: {
          username: username.value,
          password: password.value,
          confirmedPassword: confirmedPassword.value,
        },
        success(resp) {
          if (resp.error_message === "success") {
            router.push({ name: "user_account_login" });
          } else {
            error_message.value = resp.error_message;
          }
        },
      });
    };

    return {
      username,
      password,
      confirmedPassword,
      error_message,
      register,
    };
  },
};
</script>

<style scoped>
button {
  width: 100%;
}

.register-box {
  position: absolute;
  top: 55%;
  left: 50%;
  width: 400px;
  padding: 40px;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.5);
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.6);
  border-radius: 10px;
}

.register-box h2 {
  margin: 0 0 30px;
  padding: 0;
  color: #fff;
  text-align: center;
}

.register-box .user-box {
  position: relative;
}

.register-box .user-box input {
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
.register-box .user-box label {
  position: absolute;
  top: 0;
  left: 0;
  padding: 10px 0;
  font-size: 16px;
  color: #fff;
  pointer-events: none;
  transition: 0.5s;
}

.register-box .user-box input:focus ~ label,
.register-box .user-box input:valid ~ label {
  top: -20px;
  left: 0;
  color: #03e9f4;
  font-size: 12px;
}

.register-box form a {
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

.register-box a:hover {
  background: #03e9f4;
  color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 5px #03e9f4, 0 0 25px #03e9f4, 0 0 50px #03e9f4,
    0 0 100px #03e9f4;
}

.register-box a span {
  position: absolute;
  display: block;
}

.register-box a span:nth-child(1) {
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

.register-box a span:nth-child(2) {
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

.register-box a span:nth-child(3) {
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

.register-box a span:nth-child(4) {
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
