<template>
  <div class="playlists-page">
    <!-- 搜索 -->
    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="搜索">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索歌单名称"
            clearable
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 歌单列表 -->
    <el-row :gutter="20" v-loading="loading" class="playlist-list">
      <el-col :span="6" v-for="playlist in playlists" :key="playlist.id">
        <el-card class="playlist-card" @click="goToPlaylistDetail(playlist.id)">
          <img :src="getImageUrl(playlist.cover)" class="cover" />
          <div class="playlist-info">
            <h3>{{ playlist.name }}</h3>
            <p class="creator">by {{ playlist.creatorName }}</p>
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

const searchKeyword = ref('')

// 获取歌单列表
const fetchPlaylists = async () => {
  loading.value = true
  try {
    const res = await getUserPlaylistList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value
    })
    playlists.value = res.records
    total.value = res.total
  } catch (error) {
    ElMessage.error('获取歌单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchPlaylists()
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
.playlists-page {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.playlist-list {
  margin-top: 20px;
}

.playlist-card {
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.playlist-card:hover {
  transform: translateY(-5px);
}

.cover {
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
  margin: 10px 0 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.creator {
  font-size: 12px;
  color: #999;
  margin: 5px 0;
}

.description {
  font-size: 14px;
  color: #666;
  margin: 5px 0 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
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
