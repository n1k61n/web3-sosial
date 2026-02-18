import { useState } from 'react'
import { useParams } from 'react-router-dom'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useWeb3 } from '../context/Web3Context'
import { userService, postService } from '../services/api'
import PostCard from '../components/PostCard'

const Profile = () => {
  const { wallet } = useParams()
  const { account } = useWeb3()
  const [editing, setEditing] = useState(false)
  const [username, setUsername] = useState('')
  const [bio, setBio] = useState('')
  const queryClient = useQueryClient()

  const isOwner = account === wallet

  const { data: profile, refetch: refetchProfile } = useQuery({
    queryKey: ['profile', wallet],
    queryFn: () => userService.getProfile(wallet).then(r => r.data)
  })

  const { data: posts, refetch: refetchPosts } = useQuery({
    queryKey: ['userPosts', wallet],
    queryFn: () => postService.getUserPosts(wallet).then(r => r.data)
  })

  const handleFollow = async () => {
    if (!account) return
    try {
      await userService.follow({
        followerAddress: account,
        followingAddress: wallet
      })
      refetchProfile()
    } catch (err) {
      console.error(err)
    }
  }

  const handleSave = async () => {
    try {
      await userService.updateProfile(wallet, { username, bio })
      setEditing(false)
      refetchProfile()
    } catch (err) {
      console.error(err)
    }
  }

  const shortAddress = (addr) =>
    addr ? `${addr.slice(0, 6)}...${addr.slice(-4)}` : ''

  return (
    <div className="max-w-2xl mx-auto px-4 py-6">
      <div className="cyber-card p-6 mb-6 animate-fade-in">
        {/* Cover */}
        <div className="h-24 rounded-lg bg-gradient-to-r from-[#00ff88]/20 via-[#8b5cf6]/20 to-[#3b82f6]/20 mb-4 relative overflow-hidden">
          <div className="absolute inset-0"
            style={{
              backgroundImage: 'linear-gradient(45deg, #00ff8820 1px, transparent 1px), linear-gradient(-45deg, #8b5cf620 1px, transparent 1px)',
              backgroundSize: '20px 20px'
            }}
          ></div>
        </div>

        <div className="flex items-start justify-between">
          <div className="flex items-start gap-4">
            <div className="w-16 h-16 rounded-full bg-gradient-to-br from-[#00ff88] to-[#8b5cf6] flex items-center justify-center text-[#0a0a0f] font-display font-bold text-xl -mt-10 border-4 border-[#111118]">
              {wallet?.slice(2, 4).toUpperCase()}
            </div>
            <div className="mt-1">
              {editing ? (
                <input
                  value={username}
                  onChange={e => setUsername(e.target.value)}
                  placeholder="İstifadəçi adı"
                  className="cyber-input text-sm py-1 w-40"
                />
              ) : (
                <h2 className="font-display text-lg text-white">
                  {profile?.username || shortAddress(wallet)}
                </h2>
              )}
              <p className="font-display text-xs text-[#64748b] mt-0.5">{shortAddress(wallet)}</p>
            </div>
          </div>

          <div className="flex gap-2 mt-1">
            {isOwner ? (
              editing ? (
                <button onClick={handleSave} className="cyber-btn text-xs">SAXLA</button>
              ) : (
                <button
                  onClick={() => {
                    setUsername(profile?.username || '')
                    setBio(profile?.bio || '')
                    setEditing(true)
                  }}
                  className="cyber-btn-outline text-xs"
                >
                  DƏYİŞ
                </button>
              )
            ) : (
              <button onClick={handleFollow} className="cyber-btn text-xs">
                İZLƏ
              </button>
            )}
          </div>
        </div>

        <div className="mt-4">
          {editing ? (
            <textarea
              value={bio}
              onChange={e => setBio(e.target.value)}
              placeholder="Bio yaz..."
              className="cyber-input text-sm py-2 resize-none"
              rows={2}
            />
          ) : (
            <p className="text-[#64748b] text-sm font-body">
              {profile?.bio || 'Bio yoxdur'}
            </p>
          )}
        </div>

        <div className="flex gap-6 mt-4 pt-4 border-t border-[#1e1e2e]">
          <div className="text-center">
            <p className="font-display text-lg text-white">{profile?.postsCount || 0}</p>
            <p className="text-xs text-[#64748b]">Post</p>
          </div>
          <div className="text-center">
            <p className="font-display text-lg text-white">{profile?.followersCount || 0}</p>
            <p className="text-xs text-[#64748b]">İzləyici</p>
          </div>
          <div className="text-center">
            <p className="font-display text-lg text-white">{profile?.followingCount || 0}</p>
            <p className="text-xs text-[#64748b]">İzlənilən</p>
          </div>
        </div>
      </div>

      <div className="space-y-4">
        {posts?.length === 0 && (
          <div className="text-center py-16 text-[#64748b]">
            <p className="font-display text-4xl mb-4">📭</p>
            <p className="font-display">Hələ post yoxdur</p>
          </div>
        )}
        {posts?.map(post => (
          <PostCard key={post.id} post={post} onUpdate={refetchPosts} />
        ))}
      </div>
    </div>
  )
}

export default Profile
