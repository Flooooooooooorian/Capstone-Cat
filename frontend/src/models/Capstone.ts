export default interface Capstone {
  id: string
  githubApiUrl: string
  studentName: string
  url: string

  updatedDefaultAt: string
  updatedAt: string
  allCommits: number
  mainCommits: number
  allPulls: number
  openPulls: number

  qualityBadgeUrl?: string
  coverageBadgeUrl?: string
}

export type CapstoneRequest = Pick<Capstone, "githubApiUrl">
