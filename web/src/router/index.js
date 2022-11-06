import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView';
import RecordIndexView from '../views/record/RecordIndexView';
import RankListIndexView from '../views/ranklist/RankListIndexView';
import UserBotIndexView from '../views/user/bot/UserBotIndexView';
import NotFound from '../views/error/NotFound';

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
