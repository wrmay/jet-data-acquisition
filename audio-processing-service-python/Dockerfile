FROM python:3.9-slim-buster
WORKDIR /root/audio-processings-service-python
COPY requirements.txt ./
RUN pip install -r requirements.txt
COPY *.py ./
EXPOSE 9091
CMD ["python", "audio_processor.py"] 