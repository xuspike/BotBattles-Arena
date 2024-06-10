<template>
  <ContentField
    style="padding: 0; border: 1px gray"
    v-for="notice in notices"
    :key="notice.notice.id"
    :class="{ unread: notice.notice.status == 0 }"
  >
    <el-row>
      <el-col :span="2">
        <el-avatar :src="notice.senderUser.photo"></el-avatar>
      </el-col>
      <el-col :span="17">
        <span class="notice-senderName">{{ notice.senderUser.username }}</span>
        <span style="color: gray; margin-left: 2vw">{{
          notice.notice.createtime
        }}</span>
        <div style="font-size: 16px; color: lightblue">向你发起了好友申请~</div>
      </el-col>
      <el-col :span="5">
        <div v-if="notice.notice.status == 0">
          <el-button
            type="primary"
            plain
            @click="isAcceptQuest(notice.notice.id, 1)"
            >同意</el-button
          >
          <el-button
            type="danger"
            plain
            @click="isAcceptQuest(notice.notice.id, 2)"
            >拒绝</el-button
          >
        </div>
        <div v-else-if="notice.notice.status == 1">
          <el-text type="primary">你已经同意了该请求！</el-text>
        </div>
        <div v-else>
          <el-text type="danger" style="text-decoration: none"
            >你已经拒绝了该请求！</el-text
          >
        </div>
      </el-col>
    </el-row>
  </ContentField>
  <el-pagination
    style="float: right; margin-top: 1vh"
    background
    layout="prev, pager, next"
    :pager-count="6"
    :page-size="10"
    :total="noticeCount"
    v-model:current-page="currentPage"
    @current-change="pull_notices"
  />
</template>

<script scoped>
import { onMounted, ref, watch } from "vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import ContentField from "./ContentField.vue";
import $ from "jquery";
export default {
  components: {
    ContentField,
  },
  setup() {
    const notices = ref([]);
    const store = useStore();
    let currentPage = ref(1);
    let noticeCount = ref(0);
    const pull_notices = () => {
      $.ajax({
        url: "https://xrookie.xyz/api/friend/notice/getlist/",
        type: "get",
        data: {
          userId: store.state.user.id,
          page: currentPage.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            notices.value = resp.friendNotices;
            noticeCount.value = resp.noticeCount;
          }
        },
      });
    };

    const isAcceptQuest = (noticeId, operation) => {
      $.ajax({
        url: "https://xrookie.xyz/api/friend/notice/isAccept/",
        type: "post",
        data: {
          noticeId,
          operation,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.result === "success") {
            console.log("success");
          } else {
            ElMessage({
              showClose: true,
              message: resp.result,
              type: "warning",
            });
          }
          currentPage.value = 1;
          pull_notices();
        },
      });
    };

    onMounted(() => {
      if (store.state.user.id != "") {
        pull_notices();
      } else {
        watch(
          () => store.state.user.id,
          () => {
            pull_notices();
          }
        );
      }
    });
    return {
      notices,
      noticeCount,
      currentPage,
      pull_notices,
      isAcceptQuest,
    };
  },
};
</script>

<style scoped>
.notice-senderName {
  font-size: 14px;
  color: lightblue;
}

.unread {
  border: 1px solid white !important;
}
</style>
