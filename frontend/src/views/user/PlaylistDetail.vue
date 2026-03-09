<template>
  <div class="playlist-detail-page" v-loading="loading">
    <el-card v-if="playlist">
      <!-- 歌单信息 -->
      <div class="playlist-header">
        <img :src="getImageUrl(playlist.cover)" class="cover" />
        <div class="playlist-info">
          <h1>{{ playlist.name }}</h1>
          <p class="creator">创建者: {{ playlist.creatorName }}</p>
          <p class="description">{{ playlist.description }}</p>
          <div class="stats">
            <span><i class="el-icon-headset"></i> 播放 {{ playlist.playCount }} 次</span>
            <span><i class="el-icon-star-off"></i> 收藏 {{ playlist.collectCount }} 次</span>
            <span><i class="el-icon-music"></i> {{ songs.length }} 首歌曲</span>
          </div>
          <div class="tags">
            <el-tag v-if="playlist.tags">{{ playlist.tags }}</el-tag>
          </div>
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
          <el-table-column type="index" label="#" width="50" />
          <el-table-column prop="name" label="歌曲名称" />
          <el-table-column prop="artistName" label="歌手" width="150" />
          <el-table-column prop="albumName" label="专辑" width="150" />
          <el-table-column prop="duration" label="时长" width="100">
            <template #default="{ row }">
              {{ formatDuration(row.duration) }}
            </template>
          </el-table-column>
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
import { getUserPlaylistById, getSongsByPlaylistId, addFavorite, removeFavorite, checkFavorite, getFavoriteList } from '@/api'
import { getImageUrl, formatDuration } from '@/utils/format'
import { usePlayerStore } from '@/stores/player'
import type { Playlist, Song } from '@/types'

const route = useRoute()
const playerStore = usePlayerStore()

const loading = ref(false)
const playlist = ref<Playlist | null>(null)
const songs = ref<Song[]>([])
const isFavorite = ref(false)
const favoriteId = ref<number | null>(null)

// 获取歌单详情
const fetchPlaylistDetail = async () => {
  loading.value = true
  try {
    const playlistId = Number(route.params.id)

    // 获取歌单信息
    playlist.value = await getUserPlaylistById(playlistId)

    // 获取歌曲列表
    songs.value = await getSongsByPlaylistId(playlistId)

    // 检查是否已收藏并获取收藏ID
    await checkFavoriteStatus(playlistId)
  } catch (error) {
    ElMessage.error('获取歌单详情失败')
  } finally {
    loading.value = false
  }
}

// 检查收藏状态
const checkFavoriteStatus = async (playlistId: number) => {
  try {
    const favoriteStatus = await checkFavorite(3, playlistId) // type=3 表示歌单
    isFavorite.value = favoriteStatus

    // 如果已收藏，获取收藏ID
    if (favoriteStatus) {
      const favorites = await getFavoriteList(3)
      const favorite = favorites.find(f => f.targetId === playlistId)
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
  if (!playlist.value) return

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
      await addFavorite({ type: 3, targetId: playlist.value.id })
      isFavorite.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  fetchPlaylistDetail()
})
</script>

<style scoped>
.playlist-detail-page {
  padding: 20px;
}

.playlist-header {
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

.playlist-info {
  flex: 1;
}

.playlist-info h1 {
  font-size: 32px;
  margin: 0 0 15px;
}

.creator {
  font-size: 16px;
  color: #666;
  margin: 10px 0;
}

.description {
  font-size: 14px;
  line-height: 1.6;
  color: #999;
  margin: 15px 0;
}

.stats {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
  margin: 15px 0;
}

.stats span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.tags {
  margin-top: 15px;
}

.actions {
  margin-top: 15px;
}

.songs-section {
  margin-top: 40px;
}

.songs-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}
</style>
