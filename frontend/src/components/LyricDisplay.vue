<template>
  <div class="lyric-display" v-loading="loading">
    <div v-if="lyrics.length === 0" class="no-lyric">
      <el-empty description="暂无歌词" />
    </div>
    <div v-else class="lyric-content" ref="lyricContainerRef">
      <div
        v-for="(line, index) in lyrics"
        :key="index"
        class="lyric-line"
        :class="{ active: index === currentLineIndex }"
        :ref="el => setLineRef(el, index)"
      >
        {{ line.text }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, nextTick } from 'vue'
import { getUserSongLyric } from '@/api'

interface LyricLine {
  time: number
  text: string
}

const props = defineProps<{
  songId: number
  currentTime: number
}>()

const loading = ref(false)
const lyrics = ref<LyricLine[]>([])
const currentLineIndex = ref(-1)
const lyricContainerRef = ref<HTMLElement>()
const lineRefs = ref<Map<number, HTMLElement>>(new Map())

// 设置行引用
const setLineRef = (el: any, index: number) => {
  if (el) {
    lineRefs.value.set(index, el)
  }
}

// 解析 LRC 歌词
const parseLyric = (lyricText: string): LyricLine[] => {
  const lines: LyricLine[] = []
  // LRC 格式: [mm:ss.xx] 其中 xx 是百分之一秒（centiseconds）
  const timeRegex = /\[(\d{2}):(\d{2})\.(\d{2,3})\]/g

  lyricText.split('\n').forEach(line => {
    const matches = [...line.matchAll(timeRegex)]
    if (matches.length > 0) {
      const text = line.replace(timeRegex, '').trim()
      if (text) {
        matches.forEach(match => {
          const minutes = parseInt(match[1] || '0')
          const seconds = parseInt(match[2] || '0')
          // LRC 格式的小数部分是百分之一秒，需要转换为秒
          const centiseconds = (match[3] || '0').padEnd(2, '0').substring(0, 2)
          const time = minutes * 60 + seconds + parseInt(centiseconds) / 100
          lines.push({ time, text })
        })
      }
    }
  })

  return lines.sort((a, b) => a.time - b.time)
}

// 获取歌词
const fetchLyric = async () => {
  loading.value = true
  lyrics.value = []
  currentLineIndex.value = -1

  try {
    const lyricUrl = await getUserSongLyric(props.songId)
    if (lyricUrl && lyricUrl.trim()) {
      // 获取歌词文件内容
      const fullUrl = lyricUrl.startsWith('http')
        ? lyricUrl
        : `http://localhost:8080/api${lyricUrl}`

      const response = await fetch(fullUrl)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      const lyricText = await response.text()
      lyrics.value = parseLyric(lyricText)
      console.log('歌词解析完成，共', lyrics.value.length, '行')
      if (lyrics.value.length > 0) {
        console.log('前3行歌词时间:', lyrics.value.slice(0, 3).map(l => `${l.time}s: ${l.text}`))
      }
    } else {
      lyrics.value = []
    }
  } catch (error) {
    console.error('获取歌词失败:', error)
    lyrics.value = []
  } finally {
    loading.value = false
  }
}

// 滚动到当前歌词
const scrollToCurrentLine = () => {
  if (currentLineIndex.value === -1 || !lyricContainerRef.value) return

  const currentLineEl = lineRefs.value.get(currentLineIndex.value)
  if (currentLineEl) {
    const container = lyricContainerRef.value
    const lineTop = currentLineEl.offsetTop
    const containerHeight = container.clientHeight
    const lineHeight = currentLineEl.clientHeight

    // 将当前歌词滚动到容器中间
    container.scrollTo({
      top: lineTop - containerHeight / 2 + lineHeight / 2,
      behavior: 'smooth'
    })
  }
}

// 监听播放时间变化
watch(
  () => props.currentTime,
  (time) => {
    if (lyrics.value.length === 0) return

    // 找到当前应该显示的歌词行
    let index = -1
    for (let i = 0; i < lyrics.value.length; i++) {
      if (time >= lyrics.value[i].time) {
        index = i
      } else {
        break
      }
    }

    if (index !== currentLineIndex.value) {
      currentLineIndex.value = index
      if (index >= 0 && lyrics.value[index]) {
        console.log(`当前播放时间: ${time.toFixed(2)}s, 歌词: ${lyrics.value[index].text}`)
      }
      nextTick(() => {
        scrollToCurrentLine()
      })
    }
  }
)

// 监听歌曲变化
watch(
  () => props.songId,
  () => {
    currentLineIndex.value = -1
    fetchLyric()
  },
  { immediate: true }
)
</script>

<style scoped>
.lyric-display {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.no-lyric {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lyric-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.lyric-line {
  text-align: center;
  padding: 10px 0;
  font-size: 16px;
  color: #999;
  transition: all 0.3s;
  line-height: 1.8;
}

.lyric-line.active {
  color: #409eff;
  font-size: 18px;
  font-weight: bold;
  transform: scale(1.1);
}

/* 滚动条样式 */
.lyric-content::-webkit-scrollbar {
  width: 6px;
}

.lyric-content::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

.lyric-content::-webkit-scrollbar-thumb:hover {
  background-color: #c0c4cc;
}
</style>
