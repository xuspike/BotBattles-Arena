<template>
  <ContentField style="width: 60%">
    <el-tabs type="border-card">
      <el-tab-pane label="全部"
        ><el-input
          v-model="textarea"
          autosize:autosize="{ minRows: 2, maxRows: 10 }"
          type="textarea"
          placeholder="Please input"
      /></el-tab-pane>
      <el-tab-pane label="好友">好友动态</el-tab-pane>
    </el-tabs>
  </ContentField>
  <ContentField style="width: 60%"> 动态</ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import { ref } from "vue";
export default {
  components: {
    ContentField,
  },
  setup() {
    const textarea = ref("");

    return {
      textarea,
    };
  },
};
</script>

<style scoped>
::v-deep .el-input__wrapper {
  background-color: black;
}
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
