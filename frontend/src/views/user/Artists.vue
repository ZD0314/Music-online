<template>
  <div class="artists-page">
    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="搜索">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索歌手名称"
            clearable
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="语种">
          <el-select v-model="language" placeholder="全部" clearable @change="handleSearch">
            <el-option label="华语" value="华语" />
            <el-option label="欧美" value="欧美" />
            <el-option label="日韩" value="日韩" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="流派">
          <el-select v-model="genre" placeholder="全部" clearable @change="handleSearch">
            <el-option label="流行" value="流行" />
            <el-option label="摇滚" value="摇滚" />
            <el-option label="民谣" value="民谣" />
            <el-option label="电子" value="电子" />
            <el-option label="说唱" value="说唱" />
            <el-option label="古典" value="古典" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 歌手列表 -->
    <el-row :gutter="20" v-loading="loading" class="artist-list">
      <el-col :span="4" v-for="artist in artists" :key="artist.id">
        <el-card class="artist-card" @click="goToArtistDetail(artist.id)">
          <img :src="getImageUrl(artist.avatar, '/default-avatar.jpg')" class="avatar" />
          <div class="artist-info">
            <h3>{{ artist.name }}</h3>
            <p class="foreign-name">{{ artist.nameEn }}</p>
            <el-tag size="small">{{ artist.language }}</el-tag>
            <el-tag size="small" type="success">{{ artist.genre }}</el-tag>
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
import { getUserArtistList } from '@/api'
import { getImageUrl } from '@/utils/format'
import type { Artist } from '@/types'

const router = useRouter()

const loading = ref(false)
const artists = ref<Artist[]>([])
const currentPage = ref(1)
const pageSize = ref(24)
const total = ref(0)

const searchKeyword = ref('')
const language = ref('')
const genre = ref('')

// 获取歌手列表
const fetchArtists = async () => {
  loading.value = true
  try {
    const res = await getUserArtistList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value,
      language: language.value,
      genre: genre.value
    })
    artists.value = res.records
    total.value = res.total
  } catch (error) {
    ElMessage.error('获取歌手列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchArtists()
}

// 页码改变
const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchArtists()
}

// 跳转到歌手详情
const goToArtistDetail = (id: number) => {
  router.push(`/artists/${id}`)
}

onMounted(() => {
  fetchArtists()
})
</script>

<style scoped>
.artists-page {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.artist-list {
  margin-top: 20px;
}

.artist-card {
  cursor: pointer;
  margin-bottom: 20px;
  text-align: center;
  transition: transform 0.3s;
}

.artist-card:hover {
  transform: translateY(-5px);
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin: 0 auto;
}

.artist-info {
  padding: 15px 0;
}

.artist-info h3 {
  font-size: 16px;
  margin: 10px 0 5px;
}

.foreign-name {
  font-size: 12px;
  color: #999;
  margin: 5px 0 10px;
}

.el-tag {
  margin: 0 5px;
}

.pagination {
  margin-top: 30px;
  text-align: center;
}
</style>
