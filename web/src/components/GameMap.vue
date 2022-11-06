<template>
  <div ref="parent" class="gamemap">
    <canvas ref="canvas"></canvas>
  </div>
</template>

<script>
import { GameMap } from "@/assets/script/GameMap";
import { ref, onMounted } from "vue";

export default {
  setup() {
    // 绑定div和canvas
    let parent = ref(null);
    let canvas = ref(null);

    // 组件加载完后运行
    onMounted(() => {
      new GameMap(canvas.value.getContext("2d"), parent.value);
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
