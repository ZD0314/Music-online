<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">
        <h2>音乐管理系统</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/admin/artists">
          <el-icon><User /></el-icon>
          <span>歌手管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/albums">
          <el-icon><Collection /></el-icon>
          <span>专辑管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/songs">
          <el-icon><Headset /></el-icon>
          <span>歌曲管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/playlists">
          <el-icon><List /></el-icon>
          <span>歌单管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-content">
          <span class="title">{{ pageTitle }}</span>
          <div class="user-info">
            <span>{{ userStore.userInfo?.nickname }}</span>
            <el-button type="danger" size="small" @click="handleLogout">
              退出登录
            </el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { DataAnalysis, User, Collection, Headset, List } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const pageTitle = computed(() => {
  const titles: Record<string, string> = {
    '/admin/dashboard': '数据统计',
    '/admin/artists': '歌手管理',
    '/admin/albums': '专辑管理',
    '/admin/songs': '歌曲管理',
    '/admin/playlists': '歌单管理'
  }
  return titles[route.path] || '管理后台'
})

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/admin/login')
  })
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3a4b;
}

.logo h2 {
  margin: 0;
  color: #fff;
  font-size: 18px;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
