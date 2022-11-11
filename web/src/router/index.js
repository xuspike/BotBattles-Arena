import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView';
import RecordIndexView from '../views/record/RecordIndexView';
import RankListIndexView from '../views/ranklist/RankListIndexView';
import UserBotIndexView from '../views/user/bot/UserBotIndexView';
import NotFound from '../views/error/NotFound';
import UserAccountLoginView from '../views/user/account/UserAccountLoginView';
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView';

const routes = [
  {
    path: '/',
    name: "home",
    redirect: '/pk/',
  },
  {
    path: '/pk/',
    name: "pk_index",
    component: PkIndexView,
  },
  {
    path: '/record/',
    component: RecordIndexView,
    name: "record_index",
  },
  {
    path: '/ranklist/',
    component: RankListIndexView,
    name: "ranklist_index",
  },
  {
    path: '/user/account/login/',
    name: "user_account_login",
    component: UserAccountLoginView,
  },
  {
    path: '/user/account/register',
    name: "user_account_register",
    component: UserAccountRegisterView,
  },
  {
    path: '/user/bot/',
    component: UserBotIndexView,
    name: "user_bot_index",
  },
  {
    path: '/404/',
    component: NotFound,
    name: "404",
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

export default router
