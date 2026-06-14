// [프론트엔드] React 자산 관리 대시보드 화면 구현
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const App = () => {
  const [assets, setAssets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // 🌟 백엔드에서 구현한 자산 목록 API 호출
  const fetchAssets = async (pageNumber) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8080/api/v1/assets?page=${pageNumber}&size=10`);
      setAssets(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("자산 목록을 불러오는 중 오류 발생:", error);
      alert("백엔드(8080) 서버 연결 상태를 확인하거나 @CrossOrigin 설정을 체크하세요.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAssets(page);
  }, [page]);

  if (loading) return <div style={{ padding: '20px' }}>⚡ AI 가동 및 자산 마스터 데이터 로딩 중...</div>;

  return (
      <div style={{ padding: '30px', fontFamily: 'sans-serif', backgroundColor: '#fafafa', minHeight: '100vh' }}>
        <div style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
          <h2>🖥️ VisionFlow 스마트 자산 관리 대시보드</h2>
          <p style={{ color: '#666' }}>YOLO 모델이 이미지에서 탐지하여 자동으로 적재한 사내 핵심 자산 타임라인입니다.</p>

          {/* 자산 테이블 */}
          <table border="1" cellPadding="12" style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', marginTop: '20px', borderColor: '#eee' }}>
            <thead style={{ backgroundColor: '#f8f9fa' }}>
            <tr>
              <th>자산 ID</th>
              <th>자산 이름</th>
              <th>고유 자산 코드</th>
              <th>카테고리</th>
              <th>현재 상태</th>
              <th>시스템 등록 일시</th>
            </tr>
            </thead>
            <tbody>
            {assets.map((asset) => (
                <tr key={asset.assetId} style={{ borderBottom: '1px solid #eee' }}>
                  <td>{asset.assetId}</td>
                  <td><strong>{asset.assetName}</strong></td>
                  <td style={{ color: '#555', fontFamily: 'monospace', fontSize: '13px' }}>{asset.assetCode}</td>
                  <td>{asset.categoryName}</td>
                  <td>
                                    <span style={{
                                      padding: '5px 10px',
                                      borderRadius: '4px',
                                      fontSize: '13px',
                                      backgroundColor: asset.status === 'AVAILABLE' ? '#e6f4ea' : '#feeedb',
                                      color: asset.status === 'AVAILABLE' ? '#137333' : '#b06000',
                                      fontWeight: 'bold'
                                    }}>
                                        {asset.status}
                                    </span>
                  </td>
                  <td>{new Date(asset.createdAt).toLocaleString()}</td>
                </tr>
            ))}
            </tbody>
          </table>

          {/* 페이징 버튼 조작계 */}
          <div style={{ marginTop: '20px', display: 'flex', gap: '10px', justifyContent: 'center' }}>
            <button disabled={page === 0} onClick={() => setPage(page - 1)} style={{ padding: '8px 16px', cursor: 'pointer' }}>이전</button>
            <span style={{ alignSelf: 'center', fontWeight: 'bold' }}> {page + 1} / {totalPages} 페이지 </span>
            <button disabled={page >= totalPages - 1} onClick={() => setPage(page + 1)} style={{ padding: '8px 16px', cursor: 'pointer' }}>다음</button>
          </div>
        </div>
      </div>
  );
};

export default App;