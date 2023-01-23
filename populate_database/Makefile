populate:
	@echo "Downloading required packages..."
	pip install -r requirements.txt
	@echo "Downloading required packages... Done"
	@echo "Creating output directory..."
	mkdir -p output_json_files
	@echo "Creating output directory... Done"
	@echo "Populating database..."
	python3 extract_chapters.py
	@echo "Populating database... Done"