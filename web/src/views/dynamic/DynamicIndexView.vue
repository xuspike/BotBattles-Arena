<template>
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
                placeholder="Please input"
              />
            </el-col>
            <el-col :span="2">
              <el-button
                type="info"
                round
                style="margin-top: 3.5vh; margin-left: 0.8vw"
                >发布</el-button
              >
            </el-col>
          </el-row>
          <div v-if="is_upload_visiable == true"></div>
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
                placeholder="Please input"
              />
            </el-col>
            <el-col :span="2">
              <el-button
                type="info"
                round
                style="margin-top: 3.5vh; margin-left: 0.8vw"
                >发布</el-button
              >
            </el-col>
          </el-row></el-tab-pane
        >
      </el-tabs>
    </div>
  </ContentField>
  <ContentField style="width: 60%">
    <div style="color: white">动态</div>
  </ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import { ref } from "vue";
import { useStore } from "vuex";
import { Plus } from "@element-plus/icons-vue";
export default {
  components: {
    ContentField,
    Plus,
  },
  setup() {
    const textarea = ref("");
    const store = useStore();
    const is_upload_visiable = ref(false);

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

    return {
      textarea,
      store,
      fileList,
      dialogImageUrl,
      dialogVisible,
      is_upload_visiable,
      handleRemove,
      handlePictureCardPreview,
    };
  },
};
</script>

<style scoped>
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
</style>
