import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      { path: 'home', name: '首页', component: () => import('../views/Home.vue') },
      { path: 'user', name: '用户管理', component: () => import('../views/User.vue') },
      { path: 'role', name: '角色管理', component: () => import('../views/Role.vue') },
      { path: 'menu', name: '角色管理', component: () => import('../views/Menu.vue') },
      { path: 'person', name: '个人信息', component: () => import('../views/Person.vue') },
      { path: 'file', name: '文件管理', component: () => import('../views/File.vue') },
      {
        path: 'coffee', name: 'Coffee', component: () => import('../views/Coffee.vue')
      },
      {
        path: '/map',
        name: 'Map',
        component: () => import('../views/Map.vue')
      },
      {
        path: '/map2',
        name: 'Map2',
        component: () => import('../views/Map2.vue')
      }
    ]
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Front'),
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('../views/front/Home.vue')
      },
      {
        path: 'video',
        name: 'Video',
        component: () => import('../views/front/Video.vue')
      },
      {
        path: 'videoDetail',
        name: 'VideoDetail',
        component: () => import('../views/front/VideoDetail')
      },
      {
        path: 'article',
        name: 'Article',
        component: () => import('../views/front/Article')
      },
      {
        path: 'articleDetail',
        name: 'ArticleDetail',
        component: () => import('../views/front/ArticleDetail')
      },
      {
        path: 'item',
        name: 'Item',
        component: () => import('../views/front/Item')
      }
    ]
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  }


]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  next()  // 放行路由
})

export default router
