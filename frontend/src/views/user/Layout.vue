<template>
  <div class="user-layout">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo" @click="router.push('/')">
          <i class="el-icon-headset"></i>
          <span>在线音乐播放器</span>
        </div>
        <el-menu mode="horizontal" :default-active="activeMenu" router>
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/artists">歌手</el-menu-item>
          <el-menu-item index="/albums">专辑</el-menu-item>
          <el-menu-item index="/playlists">歌单</el-menu-item>
        </el-menu>
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索歌曲、歌手、专辑"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
        <div class="user-info">
          <el-button type="text" @click="goToFavorites">
            <el-icon><Star /></el-icon>
            我的收藏
          </el-button>
          <el-button type="text" @click="goToUserCenter">
            <el-icon><User /></el-icon>
            个人中心
          </el-button>
          <span>{{ userStore.userInfo?.username }}</span>
          <el-button type="text" @click="handleLogout">退出</el-button>
        </div>
      </div>
    </el-header>

    <!-- 主内容区 -->
    <el-main class="main-content">
      <router-view />
    </el-main>

    <!-- 音乐播放器 -->
    <MusicPlayer />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import MusicPlayer from '@/components/MusicPlayer.vue'
import { Search, Star, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const searchKeyword = ref('')

const activeMenu = computed(() => route.path)

// 搜索
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push(`/search?q=${encodeURIComponent(searchKeyword.value)}`)
    // 不清空搜索框，让用户可以修改关键词继续搜索
  }
}

// 跳转到收藏页
const goToFavorites = () => {
  router.push('/favorites')
}

// 跳转到个人中心
const goToUserCenter = () => {
  router.push('/center')
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.logo {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
}

.logo i {
  font-size: 28px;
  margin-right: 10px;
}

.search-box {
  flex: 1;
  max-width: 400px;
  margin: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px 20px 100px; /* 底部增加空间给播放器 */
}
</style>
