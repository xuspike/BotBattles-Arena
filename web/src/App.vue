<template>
  <NavBar />
  <router-view></router-view>
</template>

<script>
import NavBar from "./components/NavBar.vue";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
// import $ from "jquery";

export default {
  components: {
    NavBar,
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
}
</style>
