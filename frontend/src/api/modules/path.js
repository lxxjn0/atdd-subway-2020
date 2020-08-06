import ApiService from '@/api'

const PathService = {
  get(source, target, type) {
    return ApiService.get(`/paths?source=${source}&target=${target}&type=${type}`)
  }
}

export default PathService
