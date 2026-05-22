# =========================
# Build stage
# =========================
FROM node:24-alpine AS build

WORKDIR /app

# Copy dependency files first for better Docker layer caching
COPY package*.json ./

# Keep the container npm version aligned with the client workspace.
RUN npm install -g npm@11.6.1

# Install dependencies
RUN npm ci

ARG ANGULAR_CONFIGURATION=production

# Copy the rest of the project
COPY . .

# Build Angular app
RUN npm run build -- --configuration ${ANGULAR_CONFIGURATION}


# =========================
# Runtime stage
# =========================
FROM nginx:alpine

# Copy built Angular app
COPY --from=build /app/dist/client/browser /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Optional release file
RUN mkdir -p /usr/share/nginx/html/assets/ && \
    rm -f /usr/share/nginx/html/assets/release.txt && \
    date > /usr/share/nginx/html/assets/release.txt

EXPOSE 80
