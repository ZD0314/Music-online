import request from '@/utils/request'
import type {
  AdminLoginRequest,
  UserRegisterRequest,
  UserLoginRequest,
  LoginResponse,
  Statistics,
  Artist,
  Album,
  Song,
  Playlist,
  PageResult,
  SearchResult,
  Favorite
} from '@/types'

// 管理员登录
export const adminLogin = (data: AdminLoginRequest) => {
  return request.post<any, LoginResponse>('/admin/login', data)
}

// 用户注册
export const userRegister = (data: UserRegisterRequest) => {
  return request.post<any, LoginResponse>('/auth/register', data)
}

// 用户登录
export const userLogin = (data: UserLoginRequest) => {
  return request.post<any, LoginResponse>('/auth/login', data)
}

// 获取统计数据
export const getStatistics = () => {
  return request.get<any, Statistics>('/admin/statistics')
}

// 获取歌手列表
export const getArtistList = (params: {
  page: number
  size: number
  name?: string
  genre?: string
  language?: string
}) => {
  return request.get<any, PageResult<Artist>>('/admin/artists', { params })
}

// 获取歌手详情
export const getArtistById = (id: number) => {
  return request.get<any, Artist>(`/admin/artists/${id}`)
}

// 创建歌手
export const createArtist = (data: Partial<Artist>) => {
  return request.post<any, Artist>('/admin/artists', data)
}

// 更新歌手
export const updateArtist = (id: number, data: Partial<Artist>) => {
  return request.put<any, Artist>(`/admin/artists/${id}`, data)
}

// 删除歌手
export const deleteArtist = (id: number) => {
  return request.delete<any, void>(`/admin/artists/${id}`)
}

// 获取专辑列表
export const getAlbumList = (params: {
  page: number
  size: number
  name?: string
  artistId?: number
}) => {
  return request.get<any, PageResult<Album>>('/admin/albums', { params })
}

// 获取专辑详情
export const getAlbumById = (id: number) => {
  return request.get<any, Album>(`/admin/albums/${id}`)
}

// 创建专辑
export const createAlbum = (data: Partial<Album>) => {
  return request.post<any, Album>('/admin/albums', data)
}

// 更新专辑
export const updateAlbum = (id: number, data: Partial<Album>) => {
  return request.put<any, Album>(`/admin/albums/${id}`, data)
}

// 删除专辑
export const deleteAlbum = (id: number) => {
  return request.delete<any, void>(`/admin/albums/${id}`)
}

// 获取歌曲列表
export const getSongList = (params: {
  page: number
  size: number
  name?: string
  artistId?: number
  albumId?: number
}) => {
  return request.get<any, PageResult<Song>>('/admin/songs', { params })
}

// 获取歌曲详情
export const getSongById = (id: number) => {
  return request.get<any, Song>(`/admin/songs/${id}`)
}

// 创建歌曲
export const createSong = (data: Partial<Song>) => {
  return request.post<any, Song>('/admin/songs', data)
}

// 更新歌曲
export const updateSong = (id: number, data: Partial<Song>) => {
  return request.put<any, Song>(`/admin/songs/${id}`, data)
}

// 删除歌曲
export const deleteSong = (id: number) => {
  return request.delete<any, void>(`/admin/songs/${id}`)
}

// 增加播放次数
export const incrementPlayCount = (id: number) => {
  return request.post<any, void>(`/admin/songs/${id}/play`)
}

// 上传图片
export const uploadImage = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post<any, { fileName: string; fileUrl: string; fileSize: number }>(
    '/admin/upload/image',
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' }
    }
  )
}

// 上传音频
export const uploadAudio = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post<any, { fileName: string; fileUrl: string; fileSize: number }>(
    '/admin/upload/audio',
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' }
    }
  )
}

// 上传歌词
export const uploadLyric = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post<any, { fileName: string; fileUrl: string; fileSize: number }>(
    '/admin/upload/lyric',
    formData,
    {
      headers: { 'Content-Type': 'multipart/form-data' }
    }
  )
}

// 获取歌单列表
export const getPlaylistList = (params: {
  page: number
  size: number
  name?: string
}) => {
  return request.get<any, PageResult<Playlist>>('/admin/playlists', { params })
}

// 获取歌单详情
export const getPlaylistById = (id: number) => {
  return request.get<any, Playlist>(`/admin/playlists/${id}`)
}

// 创建歌单
export const createPlaylist = (data: Partial<Playlist>) => {
  return request.post<any, number>('/admin/playlists', data)
}

// 更新歌单
export const updatePlaylist = (id: number, data: Partial<Playlist>) => {
  return request.put<any, void>(`/admin/playlists/${id}`, data)
}

// 删除歌单
export const deletePlaylist = (id: number) => {
  return request.delete<any, void>(`/admin/playlists/${id}`)
}

// 添加歌曲到歌单
export const addSongToPlaylist = (playlistId: number, songId: number) => {
  return request.post<any, void>(`/admin/playlists/${playlistId}/songs`, null, {
    params: { songId }
  })
}

// 从歌单移除歌曲
export const removeSongFromPlaylist = (playlistId: number, songId: number) => {
  return request.delete<any, void>(`/admin/playlists/${playlistId}/songs/${songId}`)
}

// ==================== 用户端接口 ====================

// 获取歌手列表 - 用户端
export const getUserArtistList = (params: {
  page: number
  size: number
  keyword?: string
  initial?: string
  language?: string
  genre?: string
}) => {
  return request.get<any, PageResult<Artist>>('/api/artists', { params })
}

// 获取歌手详情 - 用户端
export const getUserArtistById = (id: number) => {
  return request.get<any, Artist>(`/api/artists/${id}`)
}

// 获取专辑列表 - 用户端
export const getUserAlbumList = (params: {
  page: number
  size: number
  keyword?: string
  artistId?: number
}) => {
  return request.get<any, PageResult<Album>>('/api/albums', { params })
}

// 获取专辑详情 - 用户端
export const getUserAlbumById = (id: number) => {
  return request.get<any, Album>(`/api/albums/${id}`)
}

// 获取歌单列表 - 用户端
export const getUserPlaylistList = (params: {
  page: number
  size: number
  keyword?: string
}) => {
  return request.get<any, PageResult<Playlist>>('/api/playlists', { params })
}

// 获取歌单详情 - 用户端
export const getUserPlaylistById = (id: number) => {
  return request.get<any, Playlist>(`/api/playlists/${id}`)
}

// 获取歌曲详情 - 用户端
export const getUserSongById = (id: number) => {
  return request.get<any, Song>(`/api/songs/${id}`)
}

// 获取歌曲歌词 - 用户端
export const getUserSongLyric = (id: number) => {
  return request.get<any, string>(`/api/songs/${id}/lyric`)
}

// 增加播放次数 - 用户端
export const userIncrementPlayCount = (id: number) => {
  return request.post<any, void>(`/api/songs/${id}/play`)
}

// 根据歌单ID获取歌曲列表
export const getSongsByPlaylistId = (playlistId: number) => {
  return request.get<any, Song[]>(`/api/songs/playlist/${playlistId}`)
}

// 根据歌手ID获取热门歌曲
export const getHotSongsByArtistId = (artistId: number, limit: number = 10) => {
  return request.get<any, Song[]>(`/api/songs/artist/${artistId}/hot`, { params: { limit } })
}

// 根据专辑ID获取歌曲列表
export const getSongsByAlbumId = (albumId: number) => {
  return request.get<any, Song[]>(`/api/songs/album/${albumId}`)
}

// ==================== 搜索接口 ====================

// 综合搜索
export const search = (keyword: string) => {
  return request.get<any, SearchResult>('/api/search', { params: { keyword } })
}

// ==================== 收藏接口 ====================

// 添加收藏
export const addFavorite = (data: { type: number; targetId: number }) => {
  return request.post<any, void>('/api/favorites', data)
}

// 取消收藏
export const removeFavorite = (id: number) => {
  return request.delete<any, void>(`/api/favorites/${id}`)
}

// 获取收藏列表
export const getFavoriteList = (type?: number) => {
  return request.get<any, Favorite[]>('/api/favorites', { params: { type } })
}

// 检查是否已收藏
export const checkFavorite = (type: number, targetId: number) => {
  return request.get<any, boolean>('/api/favorites/check', { params: { type, targetId } })
}

// ==================== 个人中心 ====================

// 获取当前用户信息
export const getUserInfo = () => {
  return request.get<any, any>('/user/info')
}

// 更新用户信息
export const updateUserInfo = (data: {
  nickname?: string
  gender?: number
  birthday?: string
  phone?: string
  email?: string
  avatar?: string
  bio?: string
}) => {
  return request.put<any, void>('/user/info', data)
}

// 修改密码
export const updatePassword = (data: { oldPassword: string; newPassword: string }) => {
  return request.put<any, void>('/user/password', data)
}

// 创建个人歌单
export const createUserPlaylist = (data: {
  name: string
  cover?: string
  description?: string
  style?: string
}) => {
  return request.post<any, void>('/user/playlists', data)
}

// 获取个人歌单列表（用户中心）
export const getMyPlaylistList = () => {
  return request.get<any, Playlist[]>('/user/playlists')
}

// 删除个人歌单
export const deleteUserPlaylist = (id: number) => {
  return request.delete<any, void>(`/user/playlists/${id}`)
}

// 更新个人歌单
export const updateUserPlaylist = (
  id: number,
  data: {
    name: string
    cover?: string
    description?: string
    style?: string
  }
) => {
  return request.put<any, void>(`/user/playlists/${id}`, data)
}


