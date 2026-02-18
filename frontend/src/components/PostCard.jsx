import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useWeb3 } from '../context/Web3Context'
import { postService } from '../services/api'

const PostCard = ({ post, onUpdate }) => {
  const { account } = useWeb3()
  const [liked, setLiked] = useState(false)
  const [showComments, setShowComments] = useState(false)
  const [comment, setComment] = useState('')
  const [comments, setComments] = useState([])
  const [loadingComments, setLoadingComments] = useState(false)

  const shortAddress = (addr) =>
    addr ? `${addr.slice(0, 6)}...${addr.slice(-4)}` : ''

  const formatDate = (date) => {
    return new Date(date).toLocaleDateString('az-AZ', {
      day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit'
    })
  }

  const handleLike = async () => {
    if (!account) return
    try {
      if (liked) {
        await postService.unlike(post.id, account)
      } else {
        await postService.like(post.id, account)
      }
      setLiked(!liked)
      onUpdate?.()
    } catch (err) {
      console.error(err)
    }
  }

  const handleComments = async () => {
    if (!showComments) {
      setLoadingComments(true)
      try {
        const res = await postService.getComments(post.id)
        setComments(res.data)
      } catch (err) {
        console.error(err)
      } finally {
        setLoadingComments(false)
      }
    }
    setShowComments(!showComments)
  }

  const handleAddComment = async () => {
    if (!comment.trim() || !account) return
    try {
      const res = await postService.addComment(post.id, {
        walletAddress: account,
        content: comment
      })
      setComments([res.data, ...comments])
      setComment('')
      onUpdate?.()
    } catch (err) {
      console.error(err)
    }
  }

  return (
    <div className="cyber-card p-5 animate-fade-in hover:border-[#2e2e4e] transition-colors">
      <div className="flex items-start gap-3 mb-4">
        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[#00ff88] to-[#8b5cf6] flex items-center justify-center text-[#0a0a0f] font-display font-bold text-sm flex-shrink-0">
          {post.walletAddress.slice(2, 4).toUpperCase()}
        </div>
        <div className="flex-1 min-w-0">
          <Link
            to={`/profile/${post.walletAddress}`}
            className="font-display text-sm text-[#00ff88] hover:underline"
          >
            {shortAddress(post.walletAddress)}
          </Link>
          <p className="text-xs text-[#64748b] mt-0.5">{formatDate(post.createdAt)}</p>
        </div>
        <span className="text-xs font-display text-[#64748b] border border-[#1e1e2e] px-2 py-1 rounded">
          {post.postType}
        </span>
      </div>

      <p className="text-[#e2e8f0] leading-relaxed mb-4 font-body">{post.content}</p>

      <div className="flex items-center gap-4 pt-3 border-t border-[#1e1e2e]">
        <button
          onClick={handleLike}
          className={`flex items-center gap-1.5 text-sm transition-colors ${
            liked ? 'text-[#00ff88]' : 'text-[#64748b] hover:text-[#00ff88]'
          }`}
        >
          <span>{liked ? '♥' : '♡'}</span>
          <span className="font-display text-xs">{post.likesCount + (liked ? 1 : 0)}</span>
        </button>

        <button
          onClick={handleComments}
          className="flex items-center gap-1.5 text-sm text-[#64748b] hover:text-[#8b5cf6] transition-colors"
        >
          <span>💬</span>
          <span className="font-display text-xs">{post.commentsCount}</span>
        </button>

        <span className="flex items-center gap-1.5 text-sm text-[#64748b]">
          <span>🔁</span>
          <span className="font-display text-xs">{post.repostsCount}</span>
        </span>
      </div>

      {showComments && (
        <div className="mt-4 space-y-3 animate-slide-up">
          {account && (
            <div className="flex gap-2">
              <input
                value={comment}
                onChange={e => setComment(e.target.value)}
                placeholder="Şərh yaz..."
                className="cyber-input text-sm py-2"
                onKeyDown={e => e.key === 'Enter' && handleAddComment()}
              />
              <button onClick={handleAddComment} className="cyber-btn text-xs px-4 whitespace-nowrap">
                GÖNDƏR
              </button>
            </div>
          )}

          {loadingComments ? (
            <p className="text-[#64748b] text-sm text-center py-2">Yüklənir...</p>
          ) : (
            comments.map(c => (
              <div key={c.id} className="bg-[#0a0a0f] rounded-lg p-3 border border-[#1e1e2e]">
                <div className="flex items-center gap-2 mb-1">
                  <span className="font-display text-xs text-[#00ff88]">{shortAddress(c.walletAddress)}</span>
                  <span className="text-xs text-[#64748b]">{formatDate(c.createdAt)}</span>
                </div>
                <p className="text-sm text-[#e2e8f0]">{c.content}</p>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  )
}

export default PostCard
