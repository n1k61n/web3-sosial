import { useState } from 'react'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useWeb3 } from '../context/Web3Context'
import { postService } from '../services/api'
import PostCard from '../components/PostCard'
import { Navigate } from 'react-router-dom'

const Feed = () => {
  const { account } = useWeb3()
  const [content, setContent] = useState('')
  const [posting, setPosting] = useState(false)
  const queryClient = useQueryClient()

  if (!account) return <Navigate to="/login" />

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['posts'],
    queryFn: () => postService.getAll().then(r => r.data)
  })

  const handlePost = async () => {
    if (!content.trim()) return
    setPosting(true)
    try {
      await postService.create({
        walletAddress: account,
        content,
        postType: 'TEXT'
      })
      setContent('')
      refetch()
    } catch (err) {
      console.error(err)
    } finally {
      setPosting(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto px-4 py-6">
      {/* Create post */}
      <div className="cyber-card p-5 mb-6 animate-fade-in">
        <div className="flex gap-3">
          <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[#00ff88] to-[#8b5cf6] flex items-center justify-center text-[#0a0a0f] font-display font-bold text-sm flex-shrink-0">
            {account.slice(2, 4).toUpperCase()}
          </div>
          <div className="flex-1">
            <textarea
              value={content}
              onChange={e => setContent(e.target.value)}
              placeholder="Nə düşünürsən?"
              rows={3}
              className="cyber-input resize-none text-sm"
            />
            <div className="flex justify-between items-center mt-3">
              <div className="flex gap-2">
                <button className="text-[#64748b] hover:text-[#00ff88] transition-colors text-sm">📷</button>
                <button className="text-[#64748b] hover:text-[#00ff88] transition-colors text-sm">🎵</button>
                <button className="text-[#64748b] hover:text-[#00ff88] transition-colors text-sm">💎</button>
              </div>
              <button
                onClick={handlePost}
                disabled={posting || !content.trim()}
                className="cyber-btn disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {posting ? 'GÖNDƏRİLİR...' : 'PAYLAŞ'}
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Posts */}
      {isLoading ? (
        <div className="space-y-4">
          {[1, 2, 3].map(i => (
            <div key={i} className="cyber-card p-5 animate-pulse">
              <div className="flex gap-3 mb-4">
                <div className="w-10 h-10 rounded-full bg-[#1e1e2e]"></div>
                <div className="flex-1">
                  <div className="h-3 bg-[#1e1e2e] rounded w-32 mb-2"></div>
                  <div className="h-2 bg-[#1e1e2e] rounded w-20"></div>
                </div>
              </div>
              <div className="space-y-2">
                <div className="h-3 bg-[#1e1e2e] rounded"></div>
                <div className="h-3 bg-[#1e1e2e] rounded w-3/4"></div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="space-y-4">
          {data?.length === 0 && (
            <div className="text-center py-16 text-[#64748b]">
              <p className="font-display text-4xl mb-4">👻</p>
              <p className="font-display">Hələ post yoxdur</p>
              <p className="text-sm mt-2">İlk postu sən yaz!</p>
            </div>
          )}
          {data?.map(post => (
            <PostCard key={post.id} post={post} onUpdate={refetch} />
          ))}
        </div>
      )}
    </div>
  )
}

export default Feed
