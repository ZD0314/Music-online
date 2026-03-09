<template>
  <div class="home-page">
    <h2 class="section-title">精选歌单</h2>
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="6" v-for="playlist in playlists" :key="playlist.id">
        <el-card class="playlist-card" @click="goToPlaylistDetail(playlist.id)">
          <img :src="getImageUrl(playlist.cover)" class="cover-image" />
          <div class="playlist-info">
            <h3>{{ playlist.name }}</h3>
            <p class="description">{{ playlist.description }}</p>
            <div class="stats">
              <span><i class="el-icon-headset"></i> {{ playlist.playCount }}</span>
              <span><i class="el-icon-star-off"></i> {{ playlist.collectCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <el-pagination
      v-if="total > 0"
      @current-change="handlePageChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
      class="pagination"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserPlaylistList } from '@/api'
import { getImageUrl } from '@/utils/format'
import type { Playlist } from '@/types'

const router = useRouter()

const loading = ref(false)
const playlists = ref<Playlist[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 获取歌单列表
const fetchPlaylists = async () => {
  loading.value = true
  try {
    const res = await getUserPlaylistList({
      page: currentPage.value,
      size: pageSize.value
    })
    playlists.value = res.records
    total.value = res.total
  } catch (error) {
    ElMessage.error('获取歌单列表失败')
  } finally {
    loading.value = false
  }
}

// 页码改变
const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchPlaylists()
}

// 跳转到歌单详情
const goToPlaylistDetail = (id: number) => {
  router.push(`/playlists/${id}`)
}

onMounted(() => {
  fetchPlaylists()
})
</script>

<style scoped>
.home-page {
  padding: 0;
}

.section-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.playlist-card {
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.playlist-card:hover {
  transform: translateY(-5px);
}

.cover-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.playlist-info {
  padding: 10px 0;
}

.playlist-info h3 {
  font-size: 16px;
  margin: 10px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.description {
  font-size: 14px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin: 5px 0;
}

.stats {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #666;
}

.stats span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination {
  margin-top: 30px;
  text-align: center;
}
</style>
