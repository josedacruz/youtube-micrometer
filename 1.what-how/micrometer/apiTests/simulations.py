import random
import requests
import time

HOST = "http://localhost:10000"
ITERATIONS = 300
DELAY_SECONDS = 2

def call_endpoint(endpoint: str):
    url = f"{HOST}{endpoint}"
    try:
        response = requests.get(url, timeout=5)
        print(f"[✓] {endpoint} → {response.status_code}")
    except requests.RequestException as e:
        print(f"[✗] {endpoint} → Error: {e}")

def main():
    print(f"Simulating {ITERATIONS} calls to /api/work and /api/observed-task every {DELAY_SECONDS}s...\n")

    for i in range(1, ITERATIONS + 1):
        print(f"--- Iteration {i} ---")
        if random.choice([True, False, True, True]):
            call_endpoint("/api/work")
        if random.choice([True, False]):
            call_endpoint("/api/observed-task")
        time.sleep(DELAY_SECONDS)

    print("\nDone. You can now check the metrics at /actuator/prometheus")

if __name__ == "__main__":
    main()
