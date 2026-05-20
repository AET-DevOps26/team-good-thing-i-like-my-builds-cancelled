FROM npm as build

RUN npm install
RUN npm build


FROM nginx
COPY --from=build dist/client/browser /usr/share/nginx/html
COPY --from=build /nginx.conf  /etc/nginx/conf.d/default.conf

#COPY src/assets/favicon /usr/share/nginx/html/

RUN mkdir -p /usr/share/nginx/html/assets/
RUN rm -f /usr/share/nginx/html/assets/release.txt
RUN touch /usr/share/nginx/html/assets/release.txt
RUN echo $(date) > /usr/share/nginx/html/assets/release.txt

EXPOSE 80
