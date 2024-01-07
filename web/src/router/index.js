import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView';
import RecordIndexView from '../views/record/RecordIndexView';
import RecordContentView from '../views/record/RecordContentView';
import RankListIndexView from '../views/ranklist/RankListIndexView';
import UserBotIndexView from '../views/user/bot/UserBotIndexView';
import NotFound from '../views/error/NotFound';
import UserAccountLoginView from '../views/user/account/UserAccountLoginView';
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView';
import UserAccountAcwingWebReceiveCode from '../views/user/account/UserAccountAcwingWebReceiveCode';
import HomeView from '../views/home/HomeView';
import store from '../store/index';

const routes = [
  {
    path: '/',
    name: "home",
    component: HomeView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: '/pk/',
    name: "pk_index",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: '/record/',
    component: RecordIndexView,
    name: "record_index",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: '/record/:recordId',
    component: RecordContentView,
    name: "record_content",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: '/ranklist/',
    component: RankListIndexView,
    name: "ranklist_index",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: '/user/account/login/',
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: '/user/account/register',
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: '/user/bot/',
    component: UserBotIndexView,
    name: "user_bot_index",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: '/user/account/acwing/web/receive_code/',
    name: "user_account_acwing_web_receive_code",
    component: UserAccountAcwingWebReceiveCode,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: '/404/',
    component: NotFound,
    name: "404",
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// to：跳转到的页面  from：从哪个页面跳转  next：将页面要不要执行下一步操作
router.beforeEach((to, from, next) => {
  const jwt_token = localStorage.getItem("jwt_token");
  let flag = true;

  if (jwt_token) {
    store.commit("updateToken", jwt_token);
    store.dispatch("getinfo", {
      success() {
        next();
      },
      error() {
        store.dispatch("logout") // 过期后就将token清空，防止下一句代码跳转到登录页面又alert一次
        router.push({name: "user_account_login"});
      },
    });
  } else {
    flag = false; // jwt过期
  }

  if(to.meta.requestAuth && !store.state.user.is_login) {
    // 由于getinfo是异步请求，如果说flag=true的话，说明还在请求
    if(flag) {
      next();
    } else {
      next({name: "user_account_login"});
    }
  } else {
    next();
  }
});

export default router
