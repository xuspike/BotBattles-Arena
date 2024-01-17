<template class="dynamic-content" v-infinite-scroll="add_dynamics">
  <ContentField style="width: 60%">
    <div class="tab-container">
      <el-tabs type="border-card">
        <el-tab-pane label="全部">
          <el-row>
            <el-col :span="2">
              <el-avatar :size="50" :src="store.state.user.photo" />
            </el-col>
            <el-col :span="20">
              <el-input
                v-model="textarea"
                autosize:autosize="{ minRows: 2, maxRows: 10 }"
                type="textarea"
                placeholder="畅所欲言吧！"
              />
            </el-col>
            <el-col :span="2">
              <el-button
                type="info"
                round
                style="margin-top: 3.5vh; margin-left: 0.8vw"
                @click="create_dynamic(-1)"
                >发布</el-button
              >
            </el-col>
          </el-row>
          <!-- <div v-if="is_upload_visiable == true"></div> -->
          <div class="demo-collapse">
            <el-collapse v-model="activeNames" @change="handleChange">
              <el-collapse-item name="1">
                <template #title>
                  <el-row style="color: white">
                    <el-col :span="22">添加照片</el-col>
                    <el-col :span="2"
                      ><el-icon><Picture /></el-icon
                    ></el-col>
                  </el-row>
                </template>
                <el-upload
                  v-model:file-list="fileList"
                  action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                  list-type="picture-card"
                  :on-preview="handlePictureCardPreview"
                  :on-remove="handleRemove"
                  style="background-color: #2e2e2e"
                >
                  <el-icon><Plus /></el-icon>
                  <el-dialog v-model="dialogVisible">
                    <img w-full :src="dialogImageUrl" alt="Preview Image" />
                  </el-dialog>
                </el-upload>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>
        <el-tab-pane label="好友"
          ><el-row>
            <el-col :span="2">
              <el-avatar :size="50" :src="store.state.user.photo" />
            </el-col>
            <el-col :span="20">
              <el-input
                v-model="textarea"
                autosize:autosize="{ minRows: 2, maxRows: 10 }"
                type="textarea"
                placeholder="畅所欲言吧！"
              />
            </el-col>
            <el-col :span="2">
              <el-button
                type="info"
                round
                style="margin-top: 3.5vh; margin-left: 0.8vw"
                >发布</el-button
              >
            </el-col> </el-row
          ><!-- <div v-if="is_upload_visiable == true"></div> -->
          <div class="demo-collapse">
            <el-collapse v-model="activeNames" @change="handleChange">
              <el-collapse-item name="1">
                <template #title>
                  <el-row style="color: white">
                    <el-col :span="22">添加照片</el-col>
                    <el-col :span="2"
                      ><el-icon><Picture /></el-icon
                    ></el-col>
                  </el-row>
                </template>
                <el-upload
                  v-model:file-list="fileList"
                  action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                  list-type="picture-card"
                  :on-preview="handlePictureCardPreview"
                  :on-remove="handleRemove"
                  style="background-color: #2e2e2e"
                >
                  <el-icon><Plus /></el-icon>
                  <el-dialog v-model="dialogVisible">
                    <img w-full :src="dialogImageUrl" alt="Preview Image" />
                  </el-dialog>
                </el-upload>
              </el-collapse-item>
            </el-collapse></div
        ></el-tab-pane>
      </el-tabs>
    </div>
  </ContentField>
  <ContentField
    v-for="dynamic in dynamics"
    :key="dynamic.parent.id"
    style="width: 60%; color: white"
  >
    <el-row>
      <el-col :span="2">
        <el-avatar :size="45" :src="dynamic.parent.userPhoto"></el-avatar>
      </el-col>
      <e-col :span="4">
        <div style="margin-left: 1vw">{{ dynamic.parent.username }}</div>
        <div style="margin-left: 1vw; color: gray; font-size: small">
          {{ dynamic.parent.pastTime }}
        </div>
      </e-col>
    </el-row>
    <el-row>
      <el-col :span="2"></el-col>
      <el-col :span="22">
        <div class="parent-content">
          {{ dynamic.parent.content }}
        </div>
      </el-col>
    </el-row>
  </ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import $ from "jquery";
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
import { Plus } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
export default {
  components: {
    ContentField,
    Plus,
  },
  setup() {
    const textarea = ref("");
    const store = useStore();
    const dynamics = ref([]);
    const is_upload_visiable = ref(false);
    let current_page = 0;
    const fileList = ref([
      {
        name: "food.jpeg",
        url: "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100",
      },
      {
        name: "food.jpeg",
        url: "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100",
      },
    ]);

    const dialogImageUrl = ref("");
    const dialogVisible = ref(false);

    const handleRemove = (uploadFile, uploadFiles) => {
      console.log(uploadFile, uploadFiles);
    };

    const handlePictureCardPreview = (uploadFile) => {
      dialogImageUrl.value = uploadFile.url;
      dialogVisible.value = true;
    };

    const create_dynamic = (reply_id) => {
      if (textarea.value === "") {
        ElMessage({
          showClose: true,
          message: "动态不可为空哦~",
          type: "warning",
        });
        return;
      }
      let photos = "";
      let flag = false;
      for (let i = 0; i < fileList.value.length; i++) {
        if (!flag) flag = true;
        else photos += "%";
        photos += fileList.value[i].url;
      }

      $.ajax({
        url: "http://127.0.0.1:3000/api/dynamic/create/",
        type: "post",
        data: {
          userId: store.state.user.id,
          replyId: reply_id,
          content: textarea.value,
          photos: photos,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          if (resp.result === "success") {
            ElMessage({
              showClose: true,
              message: "发布成功！",
              type: "success",
            });
          }
        },
      });
      current_page = 1;
      dynamics.value = [];
      pull_dynamics();
    };

    // 扩增动态数
    const add_dynamics = () => {
      current_page = current_page + 1;
      pull_dynamics();
    };

    // 将时间差转化
    const change_date = (current_date) => {
      // 将日期转化为时间差
      const currentDate = new Date(); // 获取当前时间的 Date 对象
      const serverDate = new Date(current_date);
      // 计算时间差（单位为毫秒）
      let timeDifference = currentDate.getTime() - serverDate.getTime();
      timeDifference /= 1000;
      let pastTime = "";

      if (timeDifference < 60) {
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "秒前";
      } else if (timeDifference < 3600) {
        timeDifference /= 60;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "分钟前";
      } else if (timeDifference < 3600 * 24) {
        timeDifference /= 3600;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "小时前";
      } else if (timeDifference < 3600 * 24 * 30) {
        timeDifference /= 3600 * 24;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "天前";
      } else if (timeDifference < 3600 * 24 * 30 * 12) {
        timeDifference /= 3600 * 24 * 30;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "月前";
      } else {
        timeDifference /= 3600 * 24 * 30 * 12;
        timeDifference = parseInt(timeDifference);
        pastTime = timeDifference.toString() + "年前";
      }

      return pastTime;
    };

    const pull_dynamics = () => {
      console.log("current_page = ", current_page);
      $.ajax({
        url: "http://127.0.0.1:3000/api/dynamic/getlist/",
        type: "get",
        data: {
          page: current_page,
          userId: "-1",
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          if (resp.result === "success") {
            let pastTime = "";
            for (let i = 0; i < resp.dynamics.length; i++) {
              pastTime = change_date(resp.dynamics[i].parent.createtime);
              resp.dynamics[i].parent.pastTime = pastTime;
              for (let j = 0; j < resp.dynamics[i].children.length; j++) {
                pastTime = change_date(resp.dynamics[i].children[j].createtime);
                resp.dynamics[i].children[j].pastTime = pastTime;
              }
            }

            dynamics.value = dynamics.value.concat(resp.dynamics);
            console.log(dynamics.value);
          }
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    onMounted(() => {
      pull_dynamics();
    });

    return {
      current_page,
      dynamics,
      textarea,
      store,
      fileList,
      dialogImageUrl,
      dialogVisible,
      is_upload_visiable,
      handleRemove,
      handlePictureCardPreview,
      create_dynamic,
      pull_dynamics,
      add_dynamics,
    };
  },
};
</script>

<style scoped>
.parent-content {
  margin-left: 1vw;
}
.dynamic-content {
  color: white;
  width: 60%;
  margin: auto;
  overflow: auto;
  list-style: none;
}
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

::v-deep .tab-container .el-tabs {
  background-color: #2e2e2e;
  border-color: #6b778c !important;
}

::v-deep .tab-container .el-tabs__header {
  background-color: #2e2e2e !important;
  border-color: #6b778c !important;
}

::v-deep .tab-container .el-textarea__inner {
  background-color: #2e2e2e !important;
  color: white !important;
}

::v-deep .tab-container .el-tabs__item {
  background-color: #2e2e2e !important;
  border-color: #6b778c !important;
}

::v-deep .tab-container .el-tabs__nav-scroll {
  border-color: #6b778c !important;
}

::v-deep .tab-container .el-collapse-item__header {
  background-color: #2e2e2e !important;
  border-color: #6b778c !important;
}

::v-deep .tab-container .el-collapse-item__content {
  background-color: #2e2e2e !important;
}
</style>
