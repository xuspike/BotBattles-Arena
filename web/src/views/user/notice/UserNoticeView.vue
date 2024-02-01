<template>
  <div class="notice">
    <el-row style="width: 85%; margin: auto">
      <el-col :span="6">
        <ContentField>
          <ProfileCard></ProfileCard>
        </ContentField>
      </el-col>
      <el-col :span="18">
        <ContentField style="color: white">
          <div>
            <el-tabs
              type="border-card"
              style="
                background-color: #2e2e2e;
                border-color: #2e2e2e;
                padding: 0;
              "
            >
              <el-tab-pane label="动态通知">
                <DynamicNotice></DynamicNotice>
              </el-tab-pane>
              <el-tab-pane label="好友通知">
                <FriendNotice></FriendNotice>
              </el-tab-pane>
            </el-tabs>
          </div>
        </ContentField>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ProfileCard from "../../../components/ProfileCard.vue";
import ContentField from "../../../components/ContentField.vue";
import DynamicNotice from "../../../components/DynamicNotice.vue";
import FriendNotice from "../../../components/FriendNotice.vue";
export default {
  components: {
    ProfileCard,
    ContentField,
    DynamicNotice,
    FriendNotice,
  },
};
</script>

<style scoped>
::v-deep .el-tabs--border-card > .el-tabs__content {
  padding: 0 !important;
}

::v-deep .el-tabs--border-card > .el-tabs__header {
  background-color: #2e2e2e !important;
  border-bottom: 1px solid grey !important;
}

::v-deep .el-tabs--border-card > .el-tabs__header .el-tabs__item.is-active {
  background-color: darkgray !important;
  color: black !important;
}
</style>
