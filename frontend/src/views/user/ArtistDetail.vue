<template>
  <div class="artist-detail-page" v-loading="loading">
    <el-card v-if="artist">
      <!-- 歌手信息 -->
      <div class="artist-header">
        <img :src="getImageUrl(artist.avatar, '/default-avatar.jpg')" class="avatar" />
        <div class="artist-info">
          <h1>{{ artist.name }}</h1>
          <p class="foreign-name">{{ artist.nameEn }}</p>
          <div class="tags">
            <el-tag>{{ artist.language }}</el-tag>
            <el-tag type="success">{{ artist.genre }}</el-tag>
          </div>
          <p class="bio">{{ artist.introduction }}</p>
        </div>
      </div>

      <!-- 热门歌曲 -->
      <div class="hot-songs-section">
        <h2>热门歌曲</h2>
        <el-table :data="hotSongs" style="width: 100%">
          <el-table-column type="index" label="#" width="50" />
          <el-table-column prop="name" label="歌曲名称" />
          <el-table-column prop="albumName" label="专辑" />
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

      <!-- 专辑列表 -->
      <div class="albums-section">
        <h2>专辑</h2>
        <el-row :gutter="20">
          <el-col :span="6" v-for="album in albums" :key="album.id">
            <el-card class="album-card" @click="goToAlbumDetail(album.id)">
              <img :src="getImageUrl(album.cover)" class="cover" />
              <div class="album-info">
                <h3>{{ album.name }}</h3>
                <p class="release-date">{{ album.releaseDate }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserArtistById, getHotSongsByArtistId, getUserAlbumList } from '@/api'
import { getImageUrl, formatDuration } from '@/utils/format'
import { usePlayerStore } from '@/stores/player'
import type { Artist, Song, Album } from '@/types'

const route = useRoute()
const router = useRouter()
const playerStore = usePlayerStore()

const loading = ref(false)
const artist = ref<Artist | null>(null)
const hotSongs = ref<Song[]>([])
const albums = ref<Album[]>([])

// 获取歌手详情
const fetchArtistDetail = async () => {
  loading.value = true
  try {
    const artistId = Number(route.params.id)

    // 获取歌手信息
    artist.value = await getUserArtistById(artistId)

    // 获取热门歌曲
    hotSongs.value = await getHotSongsByArtistId(artistId, 10)

    // 获取专辑列表
    const albumRes = await getUserAlbumList({
      page: 1,
      size: 100,
      artistId
    })
    albums.value = albumRes.records
  } catch (error) {
    ElMessage.error('获取歌手详情失败')
  } finally {
    loading.value = false
  }
}

// 格式化时长
// const formatDuration = (seconds: number) => {
//   const minutes = Math.floor(seconds / 60)
//   const secs = seconds % 60
//   return `${minutes}:${secs.toString().padStart(2, '0')}`
// }

// 播放歌曲
const playSong = (song: Song) => {
  playerStore.playSong(song, hotSongs.value)
}

// 跳转到专辑详情
const goToAlbumDetail = (id: number) => {
  router.push(`/albums/${id}`)
}

// 获取图片URL
// const getImageUrl = (path: string | undefined) => {
//   if (!path) return '/default-cover.jpg'
//   if (path.startsWith('http')) return path
//   return `http://localhost:8080${path}`
// }

onMounted(() => {
  fetchArtistDetail()
})
</script>

<style scoped>
.artist-detail-page {
  padding: 20px;
}

.artist-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.avatar {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  object-fit: cover;
}

.artist-info {
  flex: 1;
}

.artist-info h1 {
  font-size: 32px;
  margin: 0 0 10px;
}

.foreign-name {
  font-size: 18px;
  color: #999;
  margin: 0 0 15px;
}

.tags {
  margin-bottom: 20px;
}

.tags .el-tag {
  margin-right: 10px;
}

.bio {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
}

.hot-songs-section,
.albums-section {
  margin-top: 40px;
}

.hot-songs-section h2,
.albums-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
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
}

.release-date {
  font-size: 12px;
  color: #999;
}
</style>
