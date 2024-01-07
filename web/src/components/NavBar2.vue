<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    background-color="#010101"
    text-color="#fff"
    active-text-color="#ffd04b"
    @select="handleSelect"
  >
    <el-menu-item> King Of Bot </el-menu-item>
    <div class="flex-right" />
    <el-menu-item index="1">首页</el-menu-item>
    <el-menu-item index="2">游戏对战</el-menu-item>
    <el-menu-item index="3">对战列表</el-menu-item>
    <el-menu-item index="4">排行榜</el-menu-item>
    <el-menu-item v-if="store.state.user.is_login" index="5"
      >登录/注册</el-menu-item
    >
    <el-sub-menu index="5">
      <template #title>Workspace</template>
      <el-menu-item index="5-1">item one</el-menu-item>
      <el-menu-item index="5-2">item two</el-menu-item>
      <el-menu-item index="5-3">item three</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script>
import { ref } from "vue";
import { useStore } from "vuex";
export default {
  setup() {
    const store = useStore();
    const activeIndex = ref("1");
    const handleSelect = (key, keyPath) => {
      console.log(key, keyPath);
    };
    return {
      store,
      activeIndex,
      handleSelect,
    };
  },
};
</script>

<style>
.flex-right {
  flex-grow: 1;
}
</style>
