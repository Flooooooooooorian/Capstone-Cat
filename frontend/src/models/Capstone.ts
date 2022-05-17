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

  workflowBadgeUrl?: string
}

export type CapstoneRequest = {
  githubRepoUrl: string
}
