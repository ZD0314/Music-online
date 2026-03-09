// 统一响应格式
export interface Result<T = any> {
  code: number
  message: string
  data: T
}

// 用户信息
export interface UserInfo {
  id: number
  username: string
  nickname: string
  userType: 'admin' | 'user'
  avatar?: string
  phone?: string
  email?: string
}

// 登录响应
export interface LoginResponse {
  token: string
  userInfo: UserInfo
}

// 管理员登录请求
export interface AdminLoginRequest {
  username: string
  password: string
}

// 用户注册请求
export interface UserRegisterRequest {
  username: string
  password: string
  nickname: string
  phone: string
  email: string
}

// 用户登录请求
export interface UserLoginRequest {
  account: string
  password: string
}

// 统计数据
export interface Statistics {
  userCount: number
  artistCount: number
  songCount: number
  playlistCount: number
  activeUserCount: number
  todayPlayCount: number
}

// 歌手信息
export interface Artist {
  id: number
  name: string
  nameEn?: string
  avatar?: string
  genre: string
  language: string
  initial: string
  introduction?: string
  createTime?: string
  updateTime?: string
}

// 专辑信息
export interface Album {
  id: number
  name: string
  artistId: number
  artistName?: string
  cover?: string
  releaseDate?: string
  description?: string
  status?: number
  createTime?: string
  updateTime?: string
}

// 歌曲信息
export interface Song {
  id: number
  name: string
  artistId: number
  artistName?: string
  albumId?: number
  albumName?: string
  cover?: string
  audioUrl?: string
  lyricUrl?: string
  duration?: number
  playCount?: number
  status?: number
  createTime?: string
  updateTime?: string
}

// 分页参数
export interface PageParams {
  page: number
  size: number
}

// 歌单信息
export interface Playlist {
  id: number
  name: string
  cover?: string
  description?: string
  creatorId?: number
  creatorType?: number
  creatorName?: string
  tags?: string
  playCount?: number
  collectCount?: number
  songCount?: number
  status?: number
  createTime?: string
  updateTime?: string
}

// 分页响应
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

// 搜索结果
export interface SearchResult {
  artists: Artist[]
  albums: Album[]
  songs: Song[]
  playlists: Playlist[]
}

// 收藏信息
export interface Favorite {
  id: number
  userId: number
  type: number // 1-歌曲 2-专辑 3-歌单
  targetId: number
  targetName?: string
  targetCover?: string
  artistName?: string
  createTime?: string
}
