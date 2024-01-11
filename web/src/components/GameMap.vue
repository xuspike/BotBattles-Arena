<template>
  <div ref="parent" class="gamemap">
    <!-- 加上tabindex属性后可以读取用户操作了 -->
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { SnakeMap } from "@/assets/script/Snake/SnakeMap";
import { GoBangMap } from "@/assets/script/GoBang/GoBangMap";
import { ref, onMounted } from "vue";
import { useStore } from "vuex";
import { onBeforeRouteLeave } from "vue-router";

export default {
  setup() {
    const store = useStore();
    // 绑定div和canvas
    let parent = ref(null);
    let canvas = ref(null);
    let game_map;

    // 组件加载完后运行
    onMounted(() => {
      if (store.state.pk.mode === "snake") {
        game_map = new SnakeMap(
          canvas.value.getContext("2d"),
          parent.value,
          store
        );
        store.commit("updateGameObject", game_map);
      } else if (store.state.pk.mode === "gobang") {
        game_map = new GoBangMap(
          canvas.value.getContext("2d"),
          parent.value,
          store
        );
        store.commit("updateGameObject", game_map);
      }
    });

    onBeforeRouteLeave((to, from, next) => {
      console.log("leaving");
      console.log(game_map);
      game_map.destroy();
      next();
    });

    return {
      parent,
      canvas,
    };
  },
};
</script>

<style scoped>
div.gamemap {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
