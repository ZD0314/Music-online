<template>
  <div class="album-detail-page" v-loading="loading">
    <el-card v-if="album">
      <!-- 专辑信息 -->
      <div class="album-header">
        <img :src="getImageUrl(album.cover)" class="cover" />
        <div class="album-info">
          <h1>{{ album.name }}</h1>
          <p class="artist-name">歌手: {{ album.artistName }}</p>
          <p class="release-date">发行时间: {{ album.releaseDate }}</p>
          <p class="description">{{ album.description }}</p>
          <div class="actions">
            <el-button
              :type="isFavorite ? 'danger' : 'default'"
              @click="toggleFavorite"
            >
              <i :class="isFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
              {{ isFavorite ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 歌曲列表 -->
      <div class="songs-section">
        <h2>歌曲列表</h2>
        <el-table :data="songs" style="width: 100%">
          <el-table-column prop="trackNumber" label="序号" width="80" />
          <el-table-column prop="name" label="歌曲名称" />
          <el-table-column prop="duration" label="时长" width="100">
            <template #default="{ row }">
              {{ formatDuration(row.duration) }}
            </template>
          </el-table-column>
          <el-table-column prop="playCount" label="播放次数" width="120" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="text" @click="playSong(row)">播放</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserAlbumById, getSongsByAlbumId, addFavorite, removeFavorite, checkFavorite, getFavoriteList } from '@/api'
import { getImageUrl, formatDuration } from '@/utils/format'
import { usePlayerStore } from '@/stores/player'
import type { Album, Song } from '@/types'

const route = useRoute()
const playerStore = usePlayerStore()

const loading = ref(false)
const album = ref<Album | null>(null)
const songs = ref<Song[]>([])
const isFavorite = ref(false)
const favoriteId = ref<number | null>(null)

// 获取专辑详情
const fetchAlbumDetail = async () => {
  loading.value = true
  try {
    const albumId = Number(route.params.id)

    // 获取专辑信息
    album.value = await getUserAlbumById(albumId)

    // 获取歌曲列表
    songs.value = await getSongsByAlbumId(albumId)

    // 检查是否已收藏并获取收藏ID
    await checkFavoriteStatus(albumId)
  } catch (error) {
    ElMessage.error('获取专辑详情失败')
  } finally {
    loading.value = false
  }
}

// 检查收藏状态
const checkFavoriteStatus = async (albumId: number) => {
  try {
    const favoriteStatus = await checkFavorite(2, albumId) // type=2 表示专辑
    isFavorite.value = favoriteStatus

    // 如果已收藏，获取收藏ID
    if (favoriteStatus) {
      const favorites = await getFavoriteList(2)
      const favorite = favorites.find(f => f.targetId === albumId)
      if (favorite) {
        favoriteId.value = favorite.id
      }
    }
  } catch (error) {
    // 忽略错误
  }
}

// 播放歌曲
const playSong = (song: Song) => {
  playerStore.playSong(song, songs.value)
}

// 切换收藏状态
const toggleFavorite = async () => {
  if (!album.value) return

  try {
    if (isFavorite.value) {
      // 取消收藏
      if (favoriteId.value) {
        await removeFavorite(favoriteId.value)
        isFavorite.value = false
        favoriteId.value = null
        ElMessage.success('已取消收藏')
      }
    } else {
      // 添加收藏
      await addFavorite({ type: 2, targetId: album.value.id })
      isFavorite.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  fetchAlbumDetail()
})
</script>

<style scoped>
.album-detail-page {
  padding: 20px;
}

.album-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.cover {
  width: 250px;
  height: 250px;
  object-fit: cover;
  border-radius: 8px;
}

.album-info {
  flex: 1;
}

.album-info h1 {
  font-size: 32px;
  margin: 0 0 15px;
}

.artist-name,
.release-date {
  font-size: 16px;
  color: #666;
  margin: 10px 0;
}

.description {
  font-size: 14px;
  line-height: 1.6;
  color: #999;
  margin-top: 20px;
}

.actions {
  margin-top: 20px;
}

.songs-section {
  margin-top: 40px;
}

.songs-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}
</style>
