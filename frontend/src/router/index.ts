import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/user/Login.vue')
    },
    {
      path: '/',
      component: () => import('@/views/user/Layout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('@/views/user/Home.vue')
        },
        {
          path: 'artists',
          name: 'UserArtists',
          component: () => import('@/views/user/Artists.vue')
        },
        {
          path: 'artists/:id',
          name: 'ArtistDetail',
          component: () => import('@/views/user/ArtistDetail.vue')
        },
        {
          path: 'albums',
          name: 'UserAlbums',
          component: () => import('@/views/user/Albums.vue')
        },
        {
          path: 'albums/:id',
          name: 'AlbumDetail',
          component: () => import('@/views/user/AlbumDetail.vue')
        },
        {
          path: 'playlists',
          name: 'UserPlaylists',
          component: () => import('@/views/user/Playlists.vue')
        },
        {
          path: 'playlists/:id',
          name: 'PlaylistDetail',
          component: () => import('@/views/user/PlaylistDetail.vue')
        },
        {
          path: 'songs/:id',
          name: 'SongDetail',
          component: () => import('@/views/user/SongDetail.vue')
        },
        {
          path: 'search',
          name: 'Search',
          component: () => import('@/views/user/Search.vue')
        },
        {
          path: 'favorites',
          name: 'Favorites',
          component: () => import('@/views/user/Favorites.vue')
        },
        {
          path: 'center',
          name: 'UserCenter',
          component: () => import('@/views/user/UserCenter.vue'),
          meta: { requiresAuth: true }
        }
      ]
    },
    {
      path: '/admin/login',
      name: 'AdminLogin',
      component: () => import('@/views/admin/Login.vue')
    },
    {
      path: '/admin',
      name: 'AdminLayout',
      component: () => import('@/views/admin/Layout.vue'),
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/admin/Dashboard.vue')
        },
        {
          path: 'artists',
          name: 'Artists',
          component: () => import('@/views/admin/Artists.vue')
        },
        {
          path: 'albums',
          name: 'Albums',
          component: () => import('@/views/admin/Albums.vue')
        },
        {
          path: 'songs',
          name: 'Songs',
          component: () => import('@/views/admin/Songs.vue')
        },
        {
          path: 'playlists',
          name: 'Playlists',
          component: () => import('@/views/admin/Playlists.vue')
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth) {
    if (!userStore.isLoggedIn()) {
      // 如果需要管理员权限，跳转到管理员登录页
      if (to.meta.requiresAdmin) {
        next('/admin/login')
      } else {
        // 否则跳转到用户登录页
        next('/login')
      }
      return
    }

    if (to.meta.requiresAdmin && !userStore.isAdmin()) {
      next('/login')
      return
    }
  }

  next()
})

export default router
