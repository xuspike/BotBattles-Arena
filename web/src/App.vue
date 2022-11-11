<template>
  <Navbar />
  <router-view></router-view>
</template>

<script>
import Navbar from "./components/NavBar.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
// import $ from "jquery";

export default {
  components: {
    Navbar,
  },

  // setup() {
  //   $.ajax({
  //     url: "http://127.0.0.1:3000/user/account/token/",
  //     type: "post",
  //     data: {
  //       username: "yxc",
  //       password: "pyxc",
  //     },
  //     success(resp) {
  //       console.log(resp);
  //     },
  //     error(resp) {
  //       console.log(resp);
  //     },
  //   });

  //   $.ajax({
  //     url: "http://127.0.0.1:3000/user/account/info/",
  //     type: "get",
  //     headers: {
  //       Authorization:
  //         "Bearer " +
  //         "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0ODk2Njk0ODJkMGU0MTJjYmE0ZDg4YTMxYmI1OTIwNiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2ODE1MzI5NiwiZXhwIjoxNjY5MzYyODk2fQ.DCiYwxWReUTq01DfIsAyrYTTmfq6WgvuSmd5l6BU_kI",
  //     },
  //     success(resp) {
  //       console.log(resp);
  //     },
  //     error(resp) {
  //       console.log(resp);
  //     },
  //   });

  //   $.ajax({
  //     url: "http://127.0.0.1:3000/user/account/register/",
  //     type: "post",
  //     data: {
  //       username: "xjj",
  //       password: "123",
  //       confirmedPassword: "123",
  //     },
  //     success(resp) {
  //       console.log(resp);
  //     },
  //     error(resp) {
  //       console.log(resp);
  //     },
  //   });
  // },
};
</script>

<style>
body {
  background-image: url("./assets/images/main.png");
  background-size: cover;
}
</style>
