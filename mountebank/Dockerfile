FROM expert360/mountebank

COPY imposters /imposters

ENTRYPOINT ["mb"]
CMD ["start", "--configfile", "/imposters/imposters.ejs"]