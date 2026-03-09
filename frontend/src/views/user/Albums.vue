<template>
  <div class="albums-page">
    <!-- 搜索 -->
    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="搜索">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索专辑名称"
            clearable
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 专辑列表 -->
    <el-row :gutter="20" v-loading="loading" class="album-list">
      <el-col :span="6" v-for="album in albums" :key="album.id">
        <el-card class="album-card" @click="goToAlbumDetail(album.id)">
          <img :src="getImageUrl(album.cover)" class="cover" />
          <div class="album-info">
            <h3>{{ album.name }}</h3>
            <p class="artist-name">{{ album.artistName }}</p>
            <p class="release-date">{{ album.releaseDate }}</p>
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
import { getUserAlbumList } from '@/api'
import { getImageUrl } from '@/utils/format'
import type { Album } from '@/types'

const router = useRouter()

const loading = ref(false)
const albums = ref<Album[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const searchKeyword = ref('')

// 获取专辑列表
const fetchAlbums = async () => {
  loading.value = true
  try {
    const res = await getUserAlbumList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value
    })
    albums.value = res.records
    total.value = res.total
  } catch (error) {
    ElMessage.error('获取专辑列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchAlbums()
}

// 页码改变
const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchAlbums()
}

// 跳转到专辑详情
const goToAlbumDetail = (id: number) => {
  router.push(`/albums/${id}`)
}

onMounted(() => {
  fetchAlbums()
})
</script>

<style scoped>
.albums-page {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.album-list {
  margin-top: 20px;
}

.album-card {
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.album-card:hover {
  transform: translateY(-5px);
}

.cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.album-info {
  padding: 10px 0;
}

.album-info h3 {
  font-size: 16px;
  margin: 10px 0 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.artist-name {
  font-size: 14px;
  color: #666;
  margin: 5px 0;
}

.release-date {
  font-size: 12px;
  color: #999;
}

.pagination {
  margin-top: 30px;
  text-align: center;
}
</style>
