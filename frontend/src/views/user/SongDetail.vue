<template>
  <div class="song-detail-page" v-loading="loading">
    <el-card v-if="song">
      <!-- 歌曲信息 -->
      <div class="song-header">
        <img :src="getImageUrl(song.cover)" class="cover" />
        <div class="song-info">
          <h1>{{ song.name }}</h1>
          <p class="artist">
            歌手:
            <router-link :to="`/artists/${song.artistId}`" class="link">
              {{ song.artistName }}
            </router-link>
          </p>
          <p class="album" v-if="song.albumId">
            专辑:
            <router-link :to="`/albums/${song.albumId}`" class="link">
              {{ song.albumName }}
            </router-link>
          </p>
          <div class="stats">
            <span><i class="el-icon-headset"></i> 播放 {{ song.playCount }} 次</span>
            <span><i class="el-icon-time"></i> 时长 {{ formatDuration(song.duration) }}</span>
          </div>
          <div class="actions">
            <el-button type="primary" @click="playSong">
              <i class="el-icon-video-play"></i> 播放
            </el-button>
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

      <!-- 歌词 -->
      <div class="lyrics-section" v-if="lyrics">
        <h2>歌词</h2>
        <div class="lyrics-content">
          <p v-for="(line, index) in parsedLyrics" :key="index">{{ line }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserSongById, getUserSongLyric, addFavorite, removeFavorite, checkFavorite, getFavoriteList } from '@/api'
import { getImageUrl, formatDuration } from '@/utils/format'
import { usePlayerStore } from '@/stores/player'
import type { Song } from '@/types'

const route = useRoute()
const playerStore = usePlayerStore()

const loading = ref(false)
const song = ref<Song | null>(null)
const lyrics = ref('')
const isFavorite = ref(false)
const favoriteId = ref<number | null>(null)

// 解析歌词
const parsedLyrics = computed(() => {
  if (!lyrics.value) return []
  return lyrics.value.split('\n').filter(line => line.trim())
})

// 获取歌曲详情
const fetchSongDetail = async () => {
  loading.value = true
  try {
    const songId = Number(route.params.id)

    // 获取歌曲信息
    song.value = await getUserSongById(songId)

    // 获取歌词
    try {
      lyrics.value = await getUserSongLyric(songId)
    } catch (error) {
      // 歌词可能不存在，不显示错误
    }

    // 检查是否已收藏并获取收藏ID
    await checkFavoriteStatus(songId)
  } catch (error) {
    ElMessage.error('获取歌曲详情失败')
  } finally {
    loading.value = false
  }
}

// 检查收藏状态
const checkFavoriteStatus = async (songId: number) => {
  try {
    const favoriteStatus = await checkFavorite(1, songId) // type=1 表示歌曲
    isFavorite.value = favoriteStatus

    // 如果已收藏，获取收藏ID
    if (favoriteStatus) {
      const favorites = await getFavoriteList(1)
      const favorite = favorites.find(f => f.targetId === songId)
      if (favorite) {
        favoriteId.value = favorite.id
      }
    }
  } catch (error) {
    // 忽略错误
  }
}

// 播放歌曲
const playSong = () => {
  if (song.value) {
    playerStore.playSong(song.value, [song.value])
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  if (!song.value) return

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
      await addFavorite({ type: 1, targetId: song.value.id })
      isFavorite.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  fetchSongDetail()
})
</script>

<style scoped>
.song-detail-page {
  padding: 20px;
}

.song-header {
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

.song-info {
  flex: 1;
}

.song-info h1 {
  font-size: 32px;
  margin: 0 0 15px;
}

.artist,
.album {
  font-size: 16px;
  color: #666;
  margin: 10px 0;
}

.link {
  color: #409eff;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
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

.actions {
  margin-top: 20px;
}

.actions .el-button {
  margin-right: 10px;
}

.lyrics-section {
  margin-top: 40px;
}

.lyrics-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.lyrics-content {
  line-height: 2;
  color: #666;
  max-height: 500px;
  overflow-y: auto;
}

.lyrics-content p {
  margin: 5px 0;
}
</style>
