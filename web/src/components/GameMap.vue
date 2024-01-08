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

export default {
  setup() {
    const store = useStore();
    // 绑定div和canvas
    let parent = ref(null);
    let canvas = ref(null);

    // 组件加载完后运行
    onMounted(() => {
      if (store.state.pk.mode === "snake") {
        store.commit(
          "updateGameObject",
          new SnakeMap(canvas.value.getContext("2d"), parent.value, store)
        );
      } else if (store.state.pk.mode === "gobang") {
        store.commit(
          "updateGameObject",
          new GoBangMap(canvas.value.getContext("2d"), parent.value, store)
        );
      }
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
