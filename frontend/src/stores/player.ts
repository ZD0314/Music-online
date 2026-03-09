import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Song } from '@/types'

export type PlayMode = 'list' | 'single' | 'random'

export const usePlayerStore = defineStore('player', () => {
  // 当前播放的歌曲
  const currentSong = ref<Song | null>(null)

  // 播放列表
  const playlist = ref<Song[]>([])

  // 当前播放索引
  const currentIndex = ref(-1)

  // 是否正在播放
  const playing = ref(false)

  // 播放模式
  const playMode = ref<PlayMode>('list')

  // 音量 (0-1)
  const volume = ref(0.5)

  // 当前播放时间
  const currentTime = ref(0)

  // 歌曲总时长
  const duration = ref(0)

  // 是否显示播放列表
  const showPlaylist = ref(false)

  // 是否显示歌词
  const showLyric = ref(false)

  // 计算属性：播放模式文本
  const playModeText = computed(() => {
    switch (playMode.value) {
      case 'list':
        return '列表循环'
      case 'single':
        return '单曲循环'
      case 'random':
        return '随机播放'
      default:
        return '列表循环'
    }
  })

  // 播放歌曲
  const playSong = (song: Song, list?: Song[]) => {
    // 先设置播放列表和索引
    if (list) {
      playlist.value = list
      currentIndex.value = list.findIndex(s => s.id === song.id)
    } else if (playlist.value.length === 0) {
      playlist.value = [song]
      currentIndex.value = 0
    } else {
      const index = playlist.value.findIndex(s => s.id === song.id)
      if (index === -1) {
        playlist.value.push(song)
        currentIndex.value = playlist.value.length - 1
      } else {
        currentIndex.value = index
      }
    }

    // 先设置播放状态为 true，再设置当前歌曲
    // 这样 watch 监听器会在 playing=true 的状态下触发
    playing.value = true
    currentSong.value = song
  }

  // 暂停/播放
  const togglePlay = () => {
    playing.value = !playing.value
  }

  // 上一首
  const playPrev = () => {
    if (playlist.value.length === 0) return

    if (playMode.value === 'random') {
      const randomIndex = Math.floor(Math.random() * playlist.value.length)
      currentIndex.value = randomIndex
    } else {
      currentIndex.value = currentIndex.value - 1
      if (currentIndex.value < 0) {
        currentIndex.value = playlist.value.length - 1
      }
    }

    const song = playlist.value[currentIndex.value]
    if (song) {
      currentSong.value = song
      playing.value = true
    }
  }

  // 下一首
  const playNext = () => {
    if (playlist.value.length === 0) return

    if (playMode.value === 'random') {
      const randomIndex = Math.floor(Math.random() * playlist.value.length)
      currentIndex.value = randomIndex
    } else {
      currentIndex.value = currentIndex.value + 1
      if (currentIndex.value >= playlist.value.length) {
        currentIndex.value = 0
      }
    }

    const song = playlist.value[currentIndex.value]
    if (song) {
      currentSong.value = song
      playing.value = true
    }
  }

  // 切换播放模式
  const togglePlayMode = () => {
    const modes: PlayMode[] = ['list', 'single', 'random']
    const currentModeIndex = modes.indexOf(playMode.value)
    const nextMode = modes[(currentModeIndex + 1) % modes.length]
    if (nextMode) {
      playMode.value = nextMode
    }
  }

  // 设置音量
  const setVolume = (val: number) => {
    volume.value = val
  }

  // 设置当前播放时间
  const setCurrentTime = (time: number) => {
    currentTime.value = time
  }

  // 设置歌曲总时长
  const setDuration = (time: number) => {
    duration.value = time
  }

  // 从播放列表移除歌曲
  const removeFromPlaylist = (index: number) => {
    playlist.value.splice(index, 1)
    if (currentIndex.value === index) {
      if (playlist.value.length > 0) {
        const newSong = playlist.value[Math.min(currentIndex.value, playlist.value.length - 1)]
        if (newSong) {
          currentSong.value = newSong
        }
      } else {
        currentSong.value = null
        playing.value = false
      }
    } else if (currentIndex.value > index) {
      currentIndex.value--
    }
  }

  // 清空播放列表
  const clearPlaylist = () => {
    playlist.value = []
    currentSong.value = null
    currentIndex.value = -1
    playing.value = false
  }

  // 切换播放列表显示
  const togglePlaylist = () => {
    showPlaylist.value = !showPlaylist.value
  }

  // 切换歌词显示
  const toggleLyric = () => {
    showLyric.value = !showLyric.value
  }

  return {
    currentSong,
    playlist,
    currentIndex,
    playing,
    playMode,
    volume,
    currentTime,
    duration,
    showPlaylist,
    showLyric,
    playModeText,
    playSong,
    togglePlay,
    playPrev,
    playNext,
    togglePlayMode,
    setVolume,
    setCurrentTime,
    setDuration,
    removeFromPlaylist,
    clearPlaylist,
    togglePlaylist,
    toggleLyric
  }
})
