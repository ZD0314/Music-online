<template>
  <div class="search-page">
    <!-- 搜索框 -->
    <el-card class="search-card">
      <el-input
        v-model="keyword"
        placeholder="搜索歌曲、歌手、专辑、歌单"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button icon="el-icon-search" @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </el-card>

    <!-- 搜索结果 -->
    <div v-if="searched" v-loading="loading">
      <!-- 歌手 -->
      <el-card v-if="result.artists && result.artists.length > 0" class="result-card">
        <template #header>
          <h3>歌手</h3>
        </template>
        <el-row :gutter="20">
          <el-col :span="6" v-for="artist in result.artists" :key="artist.id">
            <div class="artist-item" @click="goToArtist(artist.id)">
              <img :src="getImageUrl(artist.avatar, '/default-avatar.jpg')" class="avatar" />
              <div class="info">
                <h4>{{ artist.name }}</h4>
                <p>{{ artist.language }} · {{ artist.genre }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 专辑 -->
      <el-card v-if="result.albums && result.albums.length > 0" class="result-card">
        <template #header>
          <h3>专辑</h3>
        </template>
        <el-row :gutter="20">
          <el-col :span="6" v-for="album in result.albums" :key="album.id">
            <div class="album-item" @click="goToAlbum(album.id)">
              <img :src="getImageUrl(album.cover)" class="cover" />
              <div class="info">
                <h4>{{ album.name }}</h4>
                <p>{{ album.artistName }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 歌曲 -->
      <el-card v-if="result.songs && result.songs.length > 0" class="result-card">
        <template #header>
          <h3>歌曲</h3>
        </template>
        <el-table :data="result.songs">
          <el-table-column type="index" label="#" width="50" />
          <el-table-column prop="name" label="歌曲名称" />
          <el-table-column prop="artistName" label="歌手" width="150" />
          <el-table-column prop="albumName" label="专辑" width="150" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="text" @click="playSong(row)">播放</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 歌单 -->
      <el-card v-if="result.playlists && result.playlists.length > 0" class="result-card">
        <template #header>
          <h3>歌单</h3>
        </template>
        <el-row :gutter="20">
          <el-col :span="6" v-for="playlist in result.playlists" :key="playlist.id">
            <div class="playlist-item" @click="goToPlaylist(playlist.id)">
              <img :src="getImageUrl(playlist.cover)" class="cover" />
              <div class="info">
                <h4>{{ playlist.name }}</h4>
                <p>{{ playlist.creatorName }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 无结果 -->
      <el-empty v-if="!hasResults" description="没有找到相关内容" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { search } from '@/api'
import { getImageUrl } from '@/utils/format'
import { usePlayerStore } from '@/stores/player'
import type { SearchResult, Song } from '@/types'

const router = useRouter()
const route = useRoute()
const playerStore = usePlayerStore()

const keyword = ref('')
const loading = ref(false)
const searched = ref(false)
const result = ref<SearchResult>({
  artists: [],
  albums: [],
  songs: [],
  playlists: []
})

const hasResults = computed(() => {
  return (
    result.value.artists.length > 0 ||
    result.value.albums.length > 0 ||
    result.value.songs.length > 0 ||
    result.value.playlists.length > 0
  )
})

// 搜索
const handleSearch = async () => {
  if (!keyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }

  loading.value = true
  searched.value = true
  try {
    result.value = await search(keyword.value)
  } catch (error) {
    ElMessage.error('搜索失败')
  } finally {
    loading.value = false
  }
}

// 跳转
const goToArtist = (id: number) => router.push(`/artists/${id}`)
const goToAlbum = (id: number) => router.push(`/albums/${id}`)
const goToPlaylist = (id: number) => router.push(`/playlists/${id}`)
const playSong = (song: Song) => {
  playerStore.playSong(song, result.value.songs)
}

// 监听路由变化
watch(
  () => route.query.q,
  (newKeyword) => {
    if (newKeyword) {
      keyword.value = newKeyword as string
      handleSearch()
    }
  },
  { immediate: true }
)
</script>

<style scoped>
.search-page {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.result-card {
  margin-bottom: 20px;
}

.result-card h3 {
  margin: 0;
  font-size: 18px;
}

.artist-item,
.album-item,
.playlist-item {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
  margin-bottom: 10px;
}

.artist-item:hover,
.album-item:hover,
.playlist-item:hover {
  background-color: #f5f5f5;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 15px;
}

.cover {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
  margin-right: 15px;
}

.info h4 {
  margin: 0 0 5px;
  font-size: 14px;
}

.info p {
  margin: 0;
  font-size: 12px;
  color: #999;
}
</style>
