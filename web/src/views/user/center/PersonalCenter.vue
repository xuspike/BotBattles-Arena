<template>
  <div style="width: 85%; margin: auto">
    <el-row :gutter="20" style="text-align: center">
      <el-col :span="8">
        <ContentField>
          <el-upload
            style="margin: auto"
            class="avatar-uploader"
            v-model:file-list="avatarList"
            list-type="picture-card"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadAvatar"
            :on-change="handleChangePic"
            :on-preview="handlePreviewPic"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="el-upload__tip">
                jpg/png files with a size less than 2MB
              </div>
            </template>
          </el-upload>
          <el-dialog v-model="showImgDialog">
            <img w-full :src="dialogimageUrl" alt="Preview Image" />
          </el-dialog>
        </ContentField>
      </el-col>
      <el-col :span="16">
        <ContentField style="display: flex; justify-content: center">
          <h2>个人信息</h2>
          <el-form ref="form" label-width="80px">
            <el-form-item label="用户名">
              <el-input
                v-model="personalEditInfo.username"
                style="width: 400px"
              ></el-input>
            </el-form-item>
            <el-form-item label="个人简介">
              <el-input
                v-model="personalEditInfo.resume"
                type="textarea"
                style="width: 400px"
              ></el-input>
            </el-form-item>
            <div style="color: red; text-align: right">{{ error_message }}</div>
            <el-form-item>
              <el-button @click.prevent="update" type="primary">修改</el-button>
            </el-form-item>
          </el-form>
        </ContentField>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import $ from "jquery";
import { Plus } from "@element-plus/icons-vue";
import ContentField from "../../../components/ContentField.vue";
export default {
  name: "PersonalCenter",
  components: {
    Plus,
    ContentField,
  },
  setup() {
    let store = useStore();
    const router = useRouter();
    let error_message = ref("");

    onMounted(() => {
      const jwt_token = localStorage.getItem("jwt_token");
      if (store.state.user.id === "" && jwt_token) {
        reloadInfo();
      }
    });

    const playerInfo = reactive({
      id: store.state.user.id,
      username: store.state.user.username,
      photo: store.state.user.photo,
      resume: store.state.user.resume,
    });

    const personalEditInfo = reactive({
      id: store.state.user.id,
      username: store.state.user.username,
      photo: store.state.user.photo,
      resume: store.state.user.resume,
    });

    let showImgDialog = ref(false);
    let dialogimageUrl = ref("");

    let avatarList = ref([
      {
        name: null,
        url: store.state.user.photo,
      },
    ]);

    console.log(avatarList);
    const reloadInfo = () => {
      store.dispatch("getinfo", {
        success() {
          store = useStore();
        },
        error() {
          alert("验证过期，请重新登录！");
          store.dispatch("logout"); // 过期后就将token清空，防止下一句代码跳转到登录页面又alert一次
          router.push({ name: "LoginView" });
        },
      });
    };

    const handlePreviewPic = (file) => {
      showImgDialog.value = true;
      dialogimageUrl.value = file.url;
    };

    const uploadAvatar = (data) => {
      let file = data.file;
      store.dispatch("uploadImage", {
        file,
        success(imgUrl) {
          personalEditInfo.photo = imgUrl;
          //+ "?x-oss-process=image/resize,h_500,m_lfit";
        },
      });
    };

    const update = () => {
      if (
        personalEditInfo.username === store.state.user.username &&
        personalEditInfo.photo === store.state.user.photo &&
        personalEditInfo.resume === store.state.user.resume
      ) {
        return;
      }

      $.ajax({
        url: "http://127.0.0.1:3000/api/user/info/update/",
        type: "post",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          userId: store.state.user.id,
          ...personalEditInfo,
        },
        success(resp) {
          if (resp.result === "success") {
            store.dispatch("getinfo", {
              success() {
                avatarList.value[0] = {
                  name:
                    ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(
                      /[018]/g,
                      (c) =>
                        (
                          c ^
                          (crypto.getRandomValues(new Uint8Array(1))[0] &
                            (15 >> (c / 4)))
                        ).toString(16)
                    ) + ".jpg",
                  url: personalEditInfo.photo,
                };

                ElMessage({
                  message: "个人信息修改成功！",
                  type: "success",
                });
              },
              error() {
                ElMessage.error("修改失败");
              },
            });
          } else {
            ElMessage.error(resp.result);
          }
        },
      });
    };

    const handleChangePic = (file, fileList) => {
      if (fileList.length > 1) {
        fileList.splice(0, 1);
      }
    };

    const beforeAvatarUpload = (rawFile) => {
      if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png") {
        ElMessage.error("Avatar picture must be JPG/PNG format!");
        return false;
      } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error("Avatar picture size can not exceed 2MB!");
        return false;
      }
      return true;
    };

    return {
      store,
      update,
      error_message,
      playerInfo,
      personalEditInfo,
      handlePreviewPic,
      handleChangePic,
      beforeAvatarUpload,
      showImgDialog,
      dialogimageUrl,
      avatarList,
      uploadAvatar,
    };
  },
};
</script>

<style scoped lang="scss">
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.avatar-uploader:deep(.el-upload-list__item-preview) {
  position: relative;
  left: 20px;
}

.avatar-uploader:deep(.el-upload-list__item-delete) {
  visibility: hidden;
}

.avatar-uploader:deep(.el-icon--close-tip) {
  visibility: hidden;
}

input[type="file"] {
  display: none;
}
</style>
