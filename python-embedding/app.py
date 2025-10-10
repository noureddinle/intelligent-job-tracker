from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer
from typing import List
import torch
import uvicorn
import os

app = FastAPI(
    title="Embedding Service",
    description="Generates embeddings for text using SentenceTransformer (all-mpnet-base-v2).",
    version="1.0.0"
)


MODEL_NAME = os.getenv("EMBEDDING_MODEL", "sentence-transformers/all-mpnet-base-v2")
DEVICE = "cuda" if torch.cuda.is_available() else "cpu"

print(f"🚀 Loading embedding model: {MODEL_NAME} on {DEVICE}")
model = SentenceTransformer(MODEL_NAME, device=DEVICE)

class TextRequest(BaseModel):
    text: str

class EmbeddingResponse(BaseModel):
    embedding: List[float]

@app.get("/")
async def root():
    return {"message": "✅ Embedding API is running!", "model": MODEL_NAME, "device": DEVICE}


@app.post("/embed", response_model=EmbeddingResponse)
async def get_embedding(req: TextRequest):
    try:
        if not req.text.strip():
            raise HTTPException(status_code=400, detail="Text cannot be empty.")

        embedding = model.encode(req.text, convert_to_numpy=True).tolist()
        return {"embedding": embedding}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Embedding error: {str(e)}")


if __name__ == "__main__":
    uvicorn.run("app:app", host="0.0.0.0", port=8001, reload=True)

