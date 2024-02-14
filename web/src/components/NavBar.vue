<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand" :to="{ name: 'home' }"
        >BotBattles-Arena</router-link
      >
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarText"
        aria-controls="navbarText"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'pk_index' }"
              >对战</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'record_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'record_index' }"
              >对战列表</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'ranklist_index' }"
              >排行榜</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'dynamic_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'dynamic_index' }"
            >
              动态
            </router-link>
          </li>
        </ul>
        <div class="notice" style="color: white; text-align: right">
          <el-badge
            @click="GoToNoticeIndex"
            :value="$store.state.user.noticeCount"
            :max="99"
            class="item"
            style="cursor: pointer"
          >
            <el-icon><Bell /></el-icon>
          </el-badge>
        </div>

        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="navbarDropdown"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              <img
                class="avatar"
                :src="$store.state.user.photo"
                alt=""
                style="width: 2vw"
              />
              {{ $store.state.user.username }}
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_personal_center' }"
                  >我的信息</router-link
                >
              </li>
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_bot_index' }"
                  >我的Bot</router-link
                >
              </li>
              <li><hr class="dropdown-divider" /></li>
              <li>
                <a class="dropdown-item" href="#" @click="logout">退出</a>
              </li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else>
          <li class="nav-item dropdown">
            <router-link
              class="nav-link"
              :to="{ name: 'user_account_login' }"
              role="button"
            >
              登录
            </router-link>
          </li>
          <li class="nav-item dropdown">
            <router-link
              class="nav-link"
              :to="{ name: 'user_account_register' }"
              role="button"
            >
              注册
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute, useRouter } from "vue-router";
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  setup() {
    const store = useStore();
    const route = useRoute();
    const router = useRouter();
    let route_name = computed(() => route.name);

    const logout = () => {
      store.dispatch("logout");
    };

    const GoToNoticeIndex = () => {
      router.push({
        name: "user_notice",
      });
    };

    return {
      route_name,
      logout,
      GoToNoticeIndex,
    };
  },
};
</script>

<style scoped>
.avatar {
  border-radius: 50%;
}

::v-deep .container .el-icon {
  margin: auto;
}

.item {
  margin-top: 10px;
  margin-right: 40px;
}

.el-dropdown {
  margin-top: 1.1rem;
}
</style>
