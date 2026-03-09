<template>
  <div class="music-player" v-if="playerStore.currentSong">
    <audio
      ref="audioRef"
      :src="getAudioUrl(playerStore.currentSong.audioUrl)"
      @timeupdate="handleTimeUpdate"
      @loadedmetadata="handleLoadedMetadata"
      @ended="handleEnded"
    />

    <div class="player-content">
      <!-- 歌曲信息 -->
      <div class="song-info">
        <img :src="getImageUrl(playerStore.currentSong.cover)" class="cover" />
        <div class="info">
          <div class="name">{{ playerStore.currentSong.name }}</div>
          <div class="artist">{{ playerStore.currentSong.artistName }}</div>
        </div>
      </div>

      <!-- 播放控制 -->
      <div class="player-controls">
        <div class="control-buttons">
          <el-button
            circle
            @click="playerStore.togglePlayMode"
            :title="playerStore.playModeText"
          >
            <el-icon>
              <Refresh v-if="playerStore.playMode === 'single'" />
              <Sort v-else-if="playerStore.playMode === 'list'" />
              <Refresh v-else />
            </el-icon>
          </el-button>
          <el-button circle @click="playerStore.playPrev">
            <el-icon><DArrowLeft /></el-icon>
          </el-button>
          <el-button
            circle
            size="large"
            @click="togglePlay"
          >
            <el-icon>
              <VideoPause v-if="playerStore.playing" />
              <VideoPlay v-else />
            </el-icon>
          </el-button>
          <el-button circle @click="playerStore.playNext">
            <el-icon><DArrowRight /></el-icon>
          </el-button>
          <el-button circle @click="playerStore.toggleLyric">
            <el-icon><Tickets /></el-icon>
          </el-button>
        </div>

        <!-- 进度条 -->
        <div class="progress-bar">
          <span class="time">{{ formatTime(playerStore.currentTime) }}</span>
          <el-slider
            v-model="progress"
            :show-tooltip="false"
            @change="handleProgressChange"
            class="slider"
          />
          <span class="time">{{ formatTime(playerStore.duration) }}</span>
        </div>
      </div>

      <!-- 右侧控制 -->
      <div class="right-controls">
        <el-button circle @click="playerStore.togglePlaylist">
          <el-icon><Menu /></el-icon>
        </el-button>
        <div class="volume-control">
          <el-icon><Mute v-if="playerStore.volume === 0" /><Microphone v-else /></el-icon>
          <el-slider
            v-model="volumeValue"
            :show-tooltip="false"
            @change="handleVolumeChange"
            class="volume-slider"
          />
        </div>
      </div>
    </div>

    <!-- 播放列表抽屉 -->
    <el-drawer
      v-model="playerStore.showPlaylist"
      title="播放列表"
      direction="rtl"
      size="400px"
    >
      <div class="playlist-header">
        <span>共 {{ playerStore.playlist.length }} 首</span>
        <el-button type="text" @click="playerStore.clearPlaylist">清空</el-button>
      </div>
      <div class="playlist-content">
        <div
          v-for="(song, index) in playerStore.playlist"
          :key="song.id"
          class="playlist-item"
          :class="{ active: index === playerStore.currentIndex }"
          @dblclick="playSongByIndex(index)"
        >
          <div class="song-name">
            <el-icon v-if="index === playerStore.currentIndex && playerStore.playing">
              <VideoPlay />
            </el-icon>
            {{ song.name }}
          </div>
          <div class="song-artist">{{ song.artistName }}</div>
          <el-button
            type="text"
            @click="playerStore.removeFromPlaylist(index)"
          >
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 歌词抽屉 -->
    <el-drawer
      v-model="playerStore.showLyric"
      title="歌词"
      direction="ltr"
      size="400px"
    >
      <LyricDisplay :song-id="playerStore.currentSong.id" :current-time="playerStore.currentTime" />
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, nextTick } from 'vue'
import {
  Mute,
  Microphone,
  VideoPlay,
  VideoPause,
  DArrowLeft,
  DArrowRight,
  Menu,
  Tickets,
  Refresh,
  Sort,
  Delete
} from '@element-plus/icons-vue'
import { usePlayerStore } from '@/stores/player'
import { getImageUrl } from '@/utils/format'
import { userIncrementPlayCount } from '@/api'
import LyricDisplay from './LyricDisplay.vue'

const playerStore = usePlayerStore()
const audioRef = ref<HTMLAudioElement>()

const progress = ref(0)
const volumeValue = ref(50)

// 获取音频URL
const getAudioUrl = (path: string | undefined) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `http://localhost:8080/api${path}`
}

// 格式化时间
const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 播放/暂停
const togglePlay = async () => {
  if (!audioRef.value) return

  try {
    if (playerStore.playing) {
      audioRef.value.pause()
      playerStore.togglePlay()
    } else {
      await audioRef.value.play()
      playerStore.togglePlay()
    }
  } catch (error) {
    console.error('播放切换失败:', error)
  }
}

// 时间更新
const handleTimeUpdate = () => {
  if (!audioRef.value) return
  playerStore.setCurrentTime(audioRef.value.currentTime)
  progress.value = (audioRef.value.currentTime / audioRef.value.duration) * 100
}

// 加载元数据
const handleLoadedMetadata = () => {
  if (!audioRef.value) return
  playerStore.setDuration(audioRef.value.duration)
}

// 播放结束
const handleEnded = async () => {
  if (playerStore.playMode === 'single') {
    // 单曲循环
    if (audioRef.value) {
      audioRef.value.currentTime = 0
      try {
        await audioRef.value.play()
      } catch (error) {
        console.error('单曲循环播放失败:', error)
      }
    }
  } else {
    // 列表循环或随机播放
    playerStore.playNext()
  }
}

// 进度条改变
const handleProgressChange = (value: number) => {
  if (!audioRef.value) return
  audioRef.value.currentTime = (value / 100) * audioRef.value.duration
}

// 音量改变
const handleVolumeChange = (value: number) => {
  if (!audioRef.value) return
  const volume = value / 100
  audioRef.value.volume = volume
  playerStore.setVolume(volume)
}

// 根据索引播放歌曲
const playSongByIndex = (index: number) => {
  const song = playerStore.playlist[index]
  if (song) {
    playerStore.currentIndex = index
    playerStore.currentSong = song
    playerStore.playing = true
  }
}

// 监听当前歌曲变化
watch(
  () => playerStore.currentSong,
  async (newSong, oldSong) => {
    if (!newSong) return

    // 等待 audioRef 初始化
    await nextTick()

    if (!audioRef.value) {
      console.error('音频元素未初始化')
      return
    }

    // 先暂停当前播放，避免 AbortError
    audioRef.value.pause()

    // 重置进度
    progress.value = 0
    playerStore.setCurrentTime(0)

    // 加载新歌曲
    audioRef.value.load()

    // 如果应该播放，等待加载完成后再播放
    if (playerStore.playing) {
      try {
        // 等待音频可以播放
        await new Promise((resolve, reject) => {
          const timeout = setTimeout(() => {
            audioRef.value!.removeEventListener('canplay', onCanPlay)
            audioRef.value!.removeEventListener('error', onError)
            reject(new Error('加载超时'))
          }, 10000) // 10秒超时

          const onCanPlay = () => {
            clearTimeout(timeout)
            audioRef.value!.removeEventListener('canplay', onCanPlay)
            audioRef.value!.removeEventListener('error', onError)
            resolve(true)
          }
          const onError = (e: Event) => {
            clearTimeout(timeout)
            audioRef.value!.removeEventListener('canplay', onCanPlay)
            audioRef.value!.removeEventListener('error', onError)
            reject(e)
          }
          audioRef.value!.addEventListener('canplay', onCanPlay, { once: true })
          audioRef.value!.addEventListener('error', onError, { once: true })
        })
        await audioRef.value.play()
        console.log('开始播放:', newSong.name)
      } catch (error) {
        console.error('播放失败:', error)
        playerStore.playing = false
      }
    }

    // 增加播放次数
    userIncrementPlayCount(newSong.id).catch(() => {})

    // 如果是新歌曲且正在播放，自动打开歌词
    if (oldSong?.id !== newSong.id && playerStore.playing) {
      playerStore.showLyric = true
    }
  }
)

// 初始化音量
if (audioRef.value) {
  audioRef.value.volume = playerStore.volume
  volumeValue.value = playerStore.volume * 100
}
</script>

<style scoped>
.music-player {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: #fff;
  border-top: 1px solid #e4e7ed;
  z-index: 1000;
}

.player-content {
  max-width: 1400px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 20px;
  gap: 20px;
}

.song-info {
  display: flex;
  align-items: center;
  gap: 15px;
  width: 250px;
}

.cover {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  object-fit: cover;
}

.info {
  flex: 1;
  overflow: hidden;
}

.name {
  font-size: 14px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.artist {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.player-controls {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.control-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.progress-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.time {
  font-size: 12px;
  color: #999;
  min-width: 40px;
}

.slider {
  flex: 1;
}

.right-controls {
  display: flex;
  align-items: center;
  gap: 15px;
  width: 200px;
  justify-content: flex-end;
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 120px;
}

.volume-slider {
  flex: 1;
}

.playlist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 10px;
}

.playlist-content {
  max-height: calc(100vh - 150px);
  overflow-y: auto;
}

.playlist-item {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.playlist-item:hover {
  background-color: #f5f5f5;
}

.playlist-item.active {
  background-color: #ecf5ff;
}

.song-name {
  flex: 1;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 5px;
}

.song-artist {
  font-size: 12px;
  color: #999;
  margin-right: 10px;
}
</style>
