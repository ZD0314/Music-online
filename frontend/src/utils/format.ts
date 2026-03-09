/**
 * 获取完整的图片URL
 */
export const getImageUrl = (path: string | undefined | any, defaultImage = '/default-cover.jpg'): string => {
  if (!path) return defaultImage
  // 如果是对象，尝试获取 fileUrl 或 data 属性
  if (typeof path === 'object') {
    path = path.fileUrl || path.data || path.url
  }
  if (!path) return defaultImage
  if (typeof path === 'string' && path.startsWith('http')) return path
  return `http://localhost:8080/api${path}`
}

/**
 * 格式化时长（秒转分:秒）
 */
export const formatDuration = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${minutes}:${secs.toString().padStart(2, '0')}`
}

/**
 * 格式化时间
 */
export const formatTime = (time: string): string => {
  return new Date(time).toLocaleString('zh-CN')
}
