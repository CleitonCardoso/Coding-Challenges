let backendHost

const hostname = window && window.location && window.location.hostname

if (hostname === 'localhost') {
  backendHost = 'http://localhost:8080'
} else if (hostname === 'production') {
  backendHost = 'productionHost'
}

export const API_ROOT = backendHost
